package top.om1ga.share.content.controller;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import top.om1ga.share.common.resp.CommonResp;
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
@RestController
@RequestMapping("/share/admin")

public class ShareAdminController {

    @Resource
    private ShareService shareService;

    @GetMapping(value = "/list")
    public List<Share> getSharesNotYet(){
        return shareService.querySharesNotYet();
    }

    @PostMapping("/audit/{id}")
    public CommonResp<Share> auditById(@PathVariable Long id, @RequestBody ShareAuditDTO auditDTO){
        CommonResp<Share> commonResp = new CommonResp<>();
        commonResp.setData(shareService.auditById(id,auditDTO));
        return commonResp;
    }
}
