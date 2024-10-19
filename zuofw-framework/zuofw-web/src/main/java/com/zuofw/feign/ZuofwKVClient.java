package com.zuofw.feign;

import com.zuofw.domain.vo.Result;
import com.zuofw.dto.AddNoteContentReqDTO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 〈〉
 *
 * @author zuowei
 * @create 2024/10/19
 * @since 1.0.0
 */
@RestController("/kv")
public interface ZuofwKVClient {

    @PostMapping("/note/content/add")
    Result<?> addNoteContent(@Validated @RequestBody AddNoteContentReqDTO addNoteContentReqDTO);
}