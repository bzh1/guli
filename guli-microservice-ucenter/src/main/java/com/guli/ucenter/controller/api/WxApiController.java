package com.guli.ucenter.controller.api;

import com.google.gson.Gson;
import com.guli.common.constants.ResultCodeEnum;
import com.guli.common.exception.GuliException;
import com.guli.common.vo.R;
import com.guli.ucenter.entity.Member;
import com.guli.ucenter.service.MemberService;
import com.guli.ucenter.util.ConstantPropertiesUtil;
import com.guli.ucenter.util.HttpClientUtils;
import com.guli.ucenter.util.JwtUtils;
import com.guli.ucenter.vo.LoginInfoVo;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

@CrossOrigin
@Controller
@RequestMapping("/api/ucenter/wx")
public class WxApiController {

    @Autowired
    private MemberService memberService;

    /**
     * 生成授权URL
     * @param session
     * @return
     */
    @GetMapping("login")
    public String genQrConnect(HttpSession session) {

        String id = session.getId();
        //微信开放平台baseUrl
        String baseUrl = "https://open.weixin.qq.com/connect/qrconnect" +
                "?appid=%s" +
                "&redirect_uri=%s" +
                "&response_type=code" +
                "&scope=snsapi_login" +
                "&state=%s" +
                "#wechat_redirect";

        //回调地址
        String redirectUrl="";

        try {
            redirectUrl = URLEncoder.encode(ConstantPropertiesUtil.WX_OPEN_REDIRECT_URL, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new GuliException(ResultCodeEnum.URL_ENCODE_ERROR);
        }

        //防止csrf跨站伪造请求
        String state = "bzh";
        session.setAttribute("wx-open-state",state);

        //生成qrcodeUrl
        String qrconnectUrl = String.format(
                baseUrl,
                ConstantPropertiesUtil.WX_OPEN_APP_ID,
                redirectUrl,
                state);

        return "redirect:"+qrconnectUrl;
    }

    /**
     * 开发回调url
     */
    @GetMapping("callback")
    public String callback(String code, String state, HttpSession session) {

        String id = session.getId();

        //获取授权临时票据code
        System.out.println("code="+code);
        System.out.println("state="+state);

        //判断state是否合法
        String stateStr = (String) session.getAttribute("wx-open-state");

        if (StringUtils.isEmpty(state) || !state.equals(stateStr) || StringUtils.isEmpty(code)) {
            System.out.println("state校验失败！");
            throw new GuliException(ResultCodeEnum.ILLEGAL_CALLBACK_REQUEST_ERROR);
        }
        System.out.println("state校验成功！");

        //第一次向微信授权服务器发起请求：换取access_token
        String baseAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token" +
                "?appid=%s" +
                "&secret=%s" +
                "&code=%s" +
                "&grant_type=authorization_code";

        String accessTokenUrl = String .format(
                baseAccessTokenUrl,
                ConstantPropertiesUtil.WX_OPEN_APP_ID,
                ConstantPropertiesUtil.WX_OPEN_APP_SECRET,
                code);

        String result=null;
        try {
            result = HttpClientUtils.get(accessTokenUrl);
            System.out.println(result);
        } catch (Exception e) {
            throw new GuliException(ResultCodeEnum.FETCH_ACCESSTOKEN_FAILD);
        }

        Gson gson = new Gson();
        HashMap<String,Object> resultMap = gson.fromJson(result, HashMap.class);
        if (resultMap.get("errcode") != null) {
            throw new GuliException(ResultCodeEnum.FETCH_ACCESSTOKEN_FAILD);
        }

        String accessToken = (String) resultMap.get("access_token");
        String openid = (String) resultMap.get("openid");

        if (StringUtils.isEmpty(accessToken) || StringUtils.isEmpty(openid)) {
            throw new GuliException(ResultCodeEnum.FETCH_ACCESSTOKEN_FAILD);
        }
        System.out.println("access token = " + accessToken);
        System.out.println("openid = " + openid);

        //根据openID返回用户信息
        Member member = memberService.getByOpenid(openid);

        //如果是新用户则从资源服务器获取个人信息并注册
        if (member == null) {

            System.out.println("注册");

            //从微信获取个人的基本信息
            String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                    "?access_token=%s" +
                    "&openid=%s";
            String userInfoUrl = String.format(baseUserInfoUrl, accessToken, openid);

            String resultUserInfo=null;
            try {
                resultUserInfo = HttpClientUtils.get(userInfoUrl);
            } catch (Exception e) {
                throw new GuliException(ResultCodeEnum.FETCH_USERINFO_ERROR);
            }

            HashMap<String,Object> resultUserInfoMap = gson.fromJson(resultUserInfo, HashMap.class);

            if (resultUserInfoMap.get("errcode") != null) {
                throw new GuliException(ResultCodeEnum.FETCH_USERINFO_ERROR);
            }

            String nickname = (String) resultUserInfoMap.get("nickname");
            String avatar = (String) resultUserInfoMap.get("headimgurl");

            member = new Member();
            member.setNickname(nickname);
            member.setAvatar(avatar);
            member.setOpenid(openid);
            memberService.save(member);

        }
        //获取jwtToken
        System.out.println("登录");
        String jwtToken = JwtUtils.genJWT(member);

        //登录成功则跳转到网站首页
        return "redirect:http://localhost:3000?token="+jwtToken;

    }

    /**
     * 解析jwtToken
     */

    @PostMapping("parse-jwt")
    @ResponseBody
    public R getLoginInfoByJwtToken(@RequestBody String jwtToken) {
        Claims claims = JwtUtils.checkJwt(jwtToken);

        String id = (String) claims.get("id");
        String nickname = (String) claims.get("nickname");
        String avatar = (String) claims.get("avatar");

        LoginInfoVo loginInfoVo = new LoginInfoVo();
        loginInfoVo.setId(id);
        loginInfoVo.setNickname(nickname);
        loginInfoVo.setAvatar(avatar);

        return R.ok().data("LoginInfoVo", loginInfoVo);
    }

}
