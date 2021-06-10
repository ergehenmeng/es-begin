package com.eghm.canal;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.CanalEntry.Column;
import com.alibaba.otter.canal.protocol.CanalEntry.Entry;
import com.alibaba.otter.canal.protocol.CanalEntry.EntryType;
import com.alibaba.otter.canal.protocol.CanalEntry.EventType;
import com.alibaba.otter.canal.protocol.CanalEntry.RowChange;
import com.alibaba.otter.canal.protocol.CanalEntry.RowData;
import com.alibaba.otter.canal.protocol.Message;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @author 殿小二
 * @date 2021/6/10
 */
@Slf4j
public class CanalListener {
    
    public static void main(String[] args) throws InterruptedException {
        CanalConnector connector = CanalConnectors.newSingleConnector(new InetSocketAddress("127.0.0.1", 11111), "example", "", "");
        connector.connect();
        connector.subscribe("explain.score");
        connector.rollback();
        int totalEmptyCount = 120;
        int emptyCount = 0;
        while (emptyCount < totalEmptyCount) {
            // 获取指定数量的数据
            Message message = connector.getWithoutAck(100);
            long batchId = message.getId();
            int size = message.getEntries().size();
            if (batchId == -1 || size == 0) {
                emptyCount++;
                log.info("等待中...");
                Thread.sleep(1000);
            } else {
                emptyCount = 0;
                printEntry(message.getEntries());
            }
            connector.ack(batchId);
        }
    }
    
    private static void printEntry(List<Entry> entryList) {
        for (Entry entry : entryList) {
            if (entry.getEntryType() == EntryType.TRANSACTIONBEGIN || entry.getEntryType() == EntryType.TRANSACTIONEND) {
                continue;
            }
            
            RowChange rowChange;
            try {
                rowChange = RowChange.parseFrom(entry.getStoreValue());
            } catch (Exception e) {
                throw new RuntimeException("解析binlog异常, data:" + entry.toString(), e);
            }
            
            EventType eventType = rowChange.getEventType();
            log.info("=== binlog信息:[{} {}], 库表信息:[{} {}] eventType:[{}] === ", entry.getHeader().getLogfileName(), entry.getHeader().getLogfileOffset(),
                    entry.getHeader().getSchemaName(), entry.getHeader().getTableName(),
                    eventType);
            for (RowData rowData : rowChange.getRowDatasList()) {
                if (eventType == EventType.DELETE) {
                    printColumn(rowData.getBeforeColumnsList());
                } else if (eventType == EventType.INSERT) {
                    printColumn(rowData.getAfterColumnsList());
                } else {
                    log.info(" == 更新之前 == ");
                    printColumn(rowData.getBeforeColumnsList());
                    log.info(" == 更新之后 ==");
                    printColumn(rowData.getAfterColumnsList());
                }
            }
        }
    }
    
    private static void printColumn(List<Column> columns) {
        for (Column column : columns) {
            log.info("字段名称:[{}] 值:[{}] 是否变更:[{}]", column.getName(), column.getValue(), column.getUpdated());
        }
    }
}
