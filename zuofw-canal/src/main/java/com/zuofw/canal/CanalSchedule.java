package com.zuofw.canal;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @description:  Canal 定时任务，用于拉取 Canal 中的数据变化
 * @author bronya
 * @date 2024/12/16 15:07
 * @version 1.0
 */
@Component
@Slf4j
public class CanalSchedule implements Runnable {

    @Resource
    private CanalProperties canalProperties;
    @Resource
    private CanalConnector canalConnector;

    @Override
    @Scheduled(fixedDelay = 100) // 每隔 100ms 被执行一次
    public void run() {
        // 初始化批次 ID，-1 表示未开始或未获取到数据
        long batchId = -1;
        try {
            // 从 canalConnector 获取批量消息，返回的数据量由 batchSize 控制，若不足，则拉取已有的
            Message message = canalConnector.getWithoutAck(canalProperties.getBatchSize());

            // 获取当前拉取消息的批次 ID
            batchId = message.getId();

            // 获取当前批次中的数据条数
            long size = message.getEntries().size();
            if (batchId == -1 || size == 0) {
                try {
                    // 拉取数据为空，休眠 1s, 防止频繁拉取
                    Thread.sleep(1000);
                } catch (InterruptedException e) {}
            } else {
                // 如果当前批次有数据，打印这批次中的数据条目
                printEntry(message.getEntries());
            }

            // 对当前批次的消息进行 ack 确认，表示该批次的数据已经被成功消费
            canalConnector.ack(batchId);
        } catch (Exception e) {
            log.error("消费 Canal 批次数据异常", e);
            // 如果出现异常，需要进行数据回滚，以便重新消费这批次的数据
            canalConnector.rollback(batchId);
        }
    }

    /**
     * 打印这一批次中的数据条目（和官方示例代码一致，后续小节中会自定义这块）
     * @param entrys
     */
    private void printEntry(List<CanalEntry.Entry> entrys) {
        for (CanalEntry.Entry entry : entrys) {
            if (entry.getEntryType() == CanalEntry.EntryType.TRANSACTIONBEGIN
                    || entry.getEntryType() == CanalEntry.EntryType.TRANSACTIONEND) {
                continue;
            }

            CanalEntry.RowChange rowChage = null;
            try {
                rowChage = CanalEntry.RowChange.parseFrom(entry.getStoreValue());
            } catch (Exception e) {
                throw new RuntimeException("ERROR ## parser of eromanga-event has an error , data:" + entry.toString(),
                        e);
            }

            CanalEntry.EventType eventType = rowChage.getEventType();
            System.out.println(String.format("================> binlog[%s:%s] , name[%s,%s] , eventType : %s",
                    entry.getHeader().getLogfileName(), entry.getHeader().getLogfileOffset(),
                    entry.getHeader().getSchemaName(), entry.getHeader().getTableName(),
                    eventType));

            for (CanalEntry.RowData rowData : rowChage.getRowDatasList()) {
                if (eventType == CanalEntry.EventType.DELETE) {
                    printColumn(rowData.getBeforeColumnsList());
                } else if (eventType == CanalEntry.EventType.INSERT) {
                    printColumn(rowData.getAfterColumnsList());
                } else {
                    System.out.println("-------> before");
                    printColumn(rowData.getBeforeColumnsList());
                    System.out.println("-------> after");
                    printColumn(rowData.getAfterColumnsList());
                }
            }
        }


    }

    /**
     * 打印字段信息
     * @param columns
     */
    private static void printColumn(List<CanalEntry.Column> columns) {
        for (CanalEntry.Column column : columns) {
            System.out.println(column.getName() + " : " + column.getValue() + "    update=" + column.getUpdated());
        }
    }

}