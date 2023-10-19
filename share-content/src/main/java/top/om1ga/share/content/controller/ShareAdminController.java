package top.om1ga.share.content.controller;

import cn.hutool.json.JSONObject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import top.om1ga.share.common.resp.CommonResp;
import top.om1ga.share.common.util.JwtUtil;
import top.om1ga.share.content.domain.dto.ShareAuditDTO;
import top.om1ga.share.content.domain.entity.Share;
import top.om1ga.share.content.service.ShareService;

import java.util.List;

/**
 * @author OM1GA
 * @version 1.0
 * @date 2023年10月13日 15:56
 * @description ShareAdminController
 */
@Tag(name = "管理员内容中心")
@RestController
@RequestMapping("/share/admin")
@Slf4j
public class ShareAdminController {

    @Resource
    private ShareService shareService;

    private final int MAX = 10;

    @Operation(summary = "获取还未审核的分享列表")
    @Parameters({@Parameter(name = "pageNo",description = "页码",in = ParameterIn.QUERY),
            @Parameter(name = "pageSize",description = "每页个数",in = ParameterIn.QUERY),
            @Parameter(name = "token",description = "请求token",in = ParameterIn.HEADER)})
    @GetMapping(value = "/list")
    public CommonResp<List<Share>> getSharesNotYet(@RequestParam(required = false,defaultValue = "1")Integer pageNo,
                                       @RequestParam(required = false,defaultValue = "8")Integer pageSize,
                                       @RequestHeader(value = "token",required = false) String token){
        if (pageSize > MAX){
            pageSize=MAX;
        }
        long userId = getUserIdFromToken(token);
        CommonResp<List<Share>> commonResp = new CommonResp<>();
        commonResp.setData(shareService.querySharesNotYet(pageNo,pageSize,userId));
        return commonResp;
    }

    @Operation(summary = "审核")
    @Parameters(@Parameter(name = "id",description = "审核人ID",in = ParameterIn.PATH))
    @PostMapping("/audit/{id}")
    public CommonResp<Share> auditById(@PathVariable Long id, @RequestBody ShareAuditDTO auditDTO){
        CommonResp<Share> commonResp = new CommonResp<>();
        commonResp.setData(shareService.auditById(id,auditDTO));
        return commonResp;
    }

    private long getUserIdFromToken(String token){
        log.info(">>>>>>>>>token"+token);
        long userId=0;
        String noToken="no-token";
        if(!noToken.equals(token)){
            JSONObject jsonObject = JwtUtil.getJSONObject(token);
            log.info("解析到 token的JSON数据为:{}",jsonObject);
            userId=Long.parseLong(jsonObject.get("id").toString());
        }else {
            log.info("没有token");
        }
        return userId;
    }
}
