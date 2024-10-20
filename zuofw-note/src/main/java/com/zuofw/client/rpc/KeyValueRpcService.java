package com.zuofw.client.rpc;



import com.zuofw.client.KVClient;
import com.zuofw.dto.AddNoteContentReqDTO;
import com.zuofw.factory.ThreadFactory;
import jakarta.annotation.Resource;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * @author: 犬小哈
 * @date: 2024/4/13 23:29
 * @version: v1.0.0
 * @description: KV 键值服务
 **/
@Component
public class KeyValueRpcService {

    @Resource
    private KVClient kvClient;

    /**
     * 保存笔记内容
     *
     * @param uuid
     * @param content
     * @return
     */
    public boolean saveNoteContent(String uuid, String content) {
        AddNoteContentReqDTO addNoteContentReqDTO = new AddNoteContentReqDTO();
        addNoteContentReqDTO.setNoteId(uuid);
        addNoteContentReqDTO.setContent(content);

//        Result<?> response = kvClient.addNoteContent(addNoteContentReqDTO);
        CompletableFuture<?> objectCompletableFuture = ThreadFactory.init().executeOne(() -> kvClient.addNoteContent(addNoteContentReqDTO));

        if (Objects.isNull(objectCompletableFuture) || !objectCompletableFuture.isDone()) {
            return false;
        }

        return true;
    }

    /**
     * 删除笔记内容
     *
     * @param uuid
     * @return
     */
    public boolean deleteNoteContent(String uuid) {
        CompletableFuture<?> objectCompletableFuture = ThreadFactory.init().executeOne(() -> kvClient.deleteNoteContent(uuid));

        if (Objects.isNull(objectCompletableFuture) || !objectCompletableFuture.isDone()) {
            return false;
        }
        return true;
    }

}
