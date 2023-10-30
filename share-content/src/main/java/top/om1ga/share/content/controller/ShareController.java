package top.om1ga.share.content.controller;

import cn.hutool.json.JSONObject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.om1ga.share.common.resp.CommonResp;
import top.om1ga.share.common.util.JwtUtil;
import top.om1ga.share.content.domain.dto.ExchangeDTO;
import top.om1ga.share.content.domain.dto.ShareRequestDTO;
import top.om1ga.share.content.domain.entity.Notice;
import top.om1ga.share.content.domain.entity.Share;
import top.om1ga.share.content.domain.resp.ShareResp;
import top.om1ga.share.content.feign.UserService;
import top.om1ga.share.content.service.NoticeService;
import top.om1ga.share.content.service.ShareService;

import java.util.List;

/**
 * @author OM1GA
 * @version 1.0
 * @date 2023年10月08日 15:03
 * @description ShareController
 */
@Tag(name = "内容中心")
@RefreshScope
@RestController
@RequestMapping(value = "/share")
@Slf4j
public class ShareController {

    @Value(value = "${systemUpdateNotice}")
    private Boolean systemUpdateNotice;

    @Resource
    private NoticeService noticeService;

    @Resource
    private ShareService shareService;
    @Resource
    private UserService userService;

    private final int MAX = 10;


    @Operation(summary = "更新头像")
    @Parameters({@Parameter(name = "file",description = "文件",in= ParameterIn.QUERY),
                 @Parameter(name = "token",description = "请求token",in = ParameterIn.HEADER)})
    @PostMapping("/uploadAvatar")
    public CommonResp<String> uploadAvatar(@RequestHeader(value = "token",required = false) String token,MultipartFile file){
        String s = shareService.uploadAvatar(file);
        String msg = userService.updateAvatar(token, s).getData();
        if ("更新失败".equals(msg)) {
            throw new IllegalArgumentException("更新头像出错");
        }
        CommonResp<String> commonResp = new CommonResp<>();
        commonResp.setData(s);
        return commonResp;
    }

    @Operation(summary = "我的投稿列表")
    @Parameters({@Parameter(name = "pageNo",description = "页码",in = ParameterIn.QUERY),
            @Parameter(name = "pageSize",description = "每页个数",in = ParameterIn.QUERY),
            @Parameter(name = "token",description = "请求token",in = ParameterIn.HEADER)})
    @GetMapping("/my-contribute")
    public CommonResp<List<Share>> myContribute(@RequestParam(required = false,defaultValue = "1")Integer pageNo,
                                                @RequestParam(required = false,defaultValue = "8")Integer pageSize,
                                                @RequestHeader(value = "token",required = false) String token){
        if (pageSize > MAX){
            pageSize=MAX;
        }
        long userId = getUserIdFromToken(token);
        CommonResp<List<Share>> commonResp = new CommonResp<>();
        commonResp.setData(shareService.myContribute(pageNo,pageSize,userId));
        return commonResp;

    }

    @Operation(summary = "投稿")
    @Parameters(@Parameter(name = "token",description = "请求token",in = ParameterIn.HEADER))
    @PostMapping("/contribute")
    public int contributeShare(@RequestBody ShareRequestDTO shareRequestDTO,
                               @RequestHeader(value = "token",required = false)String token){
        long userId = getUserIdFromToken(token);
        shareRequestDTO.setUserId(userId);
        System.out.println(shareRequestDTO);
        return shareService.contribute(shareRequestDTO);
    }

    @Operation(summary = "获取最新公告")
    @GetMapping("/notice")
    public CommonResp<Notice> getLatestNotice(){
        System.out.println(systemUpdateNotice);
        CommonResp<Notice> commonResp = new CommonResp<>();
        if (systemUpdateNotice){
            commonResp.setData(Notice.builder().content("系统正在维护中...").build());
            return commonResp;
        }
        commonResp.setData(noticeService.getLatest());
        return commonResp;
    }

    @Operation(summary = "获取分享列表")
    @Parameters({
            @Parameter(name = "title",description = "标题",in = ParameterIn.QUERY),
            @Parameter(name = "pageNo",description = "页码",in = ParameterIn.QUERY),
            @Parameter(name = "pageSize",description = "每页个数",in = ParameterIn.QUERY),
            @Parameter(name = "token",description = "请求token",in = ParameterIn.HEADER)})
    @GetMapping("/list")
    public CommonResp<List<Share>> getShareList(@RequestParam(required = false) String title,
                                                @RequestParam(required = false,defaultValue = "1")Integer pageNo,
                                                @RequestParam(required = false,defaultValue = "3")Integer pageSize,
                                                @RequestHeader(value = "token",required = false) String token){
        if(pageSize>MAX){
            pageSize=MAX;
        }
        long userId = getUserIdFromToken(token);
        CommonResp<List<Share>> commonResp = new CommonResp<>();
        commonResp.setData(shareService.getList(title,pageNo,pageSize,userId));
        return commonResp;
    }

    @Operation(summary = "我的兑换列表")
    @Parameters({@Parameter(name = "pageNo",description = "页码",in = ParameterIn.QUERY),
            @Parameter(name = "pageSize",description = "每页个数",in = ParameterIn.QUERY),
            @Parameter(name = "token",description = "请求token",in = ParameterIn.HEADER)})
    @GetMapping("/exchange/list")
    public CommonResp<List<Share>> getExchangeList(
                                                @RequestParam(required = false,defaultValue = "1")Integer pageNo,
                                                @RequestParam(required = false,defaultValue = "8")Integer pageSize,
                                                @RequestHeader(value = "token",required = false) String token){
        if(pageSize>MAX){
            pageSize=MAX;
        }
        long userId = getUserIdFromToken(token);
        CommonResp<List<Share>> commonResp = new CommonResp<>();
        commonResp.setData(shareService.getExchangedList(pageNo,pageSize,userId));
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

    @Operation(summary = "查询单个分享信息")
    @Parameters(@Parameter(name = "id",description = "id",in = ParameterIn.PATH))
    @GetMapping("/{id}")
    public CommonResp<ShareResp> getShareById(@PathVariable Long id){
        ShareResp shareResp = shareService.findById(id);
        CommonResp<ShareResp> commonResp = new CommonResp<>();
        commonResp.setData(shareResp);
        return commonResp;
    }

    @Operation(summary ="兑换")
    @PostMapping("/exchange")
    public CommonResp<Share> exchange(@RequestBody ExchangeDTO exchangeDTO){
        System.out.println(exchangeDTO);
        CommonResp<Share> commonResp = new CommonResp<>();
        commonResp.setData(shareService.exchange(exchangeDTO));
        return commonResp;
    }
}
