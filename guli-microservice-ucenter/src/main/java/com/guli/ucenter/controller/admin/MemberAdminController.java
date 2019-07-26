package com.guli.ucenter.controller.admin;

import com.guli.common.vo.R;
import com.guli.ucenter.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
@Api("会员管理")
@CrossOrigin
@RestController
@RequestMapping("/admin/ucenter/member")
public class MemberAdminController {
    @Autowired
    private MemberService memberService;

    @ApiOperation(value = "今日注册数")
    @GetMapping("count-register/{day}")
    public R registerCount(
            @ApiParam(name = "day",value = "统计日期")
            @PathVariable("day")String day
    ) {

        Integer count = memberService.countRegisterByDay(day);

        return R.ok().data("countRegister", count);

    }
}
