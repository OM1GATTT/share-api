package top.om1ga.share.content.controller;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
@RequestMapping("/admin/share")

public class ShareAdminController {

    @Resource
    private ShareService shareService;

    @GetMapping(value = "/list")
    public List<Share> getSharesNotYet(){
        return shareService.querySharesNotYet();
    }
}
