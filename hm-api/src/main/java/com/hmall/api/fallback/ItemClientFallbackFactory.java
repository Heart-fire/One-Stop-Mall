package com.hmall.api.fallback;

import com.hmall.api.client.ItemClient;
import com.hmall.api.dto.ItemDTO;
import com.hmall.api.dto.OrderDetailDTO;
import com.hmall.common.utils.CollUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.Collection;
import java.util.List;
@Slf4j
public class ItemClientFallbackFactory implements FallbackFactory<ItemClient> {

    @Override
    public ItemClient create(Throwable cause) {
        //写降级逻辑，调用create方法，返回ItemClient对象，就是他的Fallback对象
        return new ItemClient() {
            @Override
            public List<ItemDTO> queryItemByIds(Collection<Long> itemIds) {
                log.error("查询商品失败！:",cause);
                // 在ItemClient所有接口中编写失败处理逻辑，查询失败后返回什么,可以返回空集合
                return CollUtils.emptyList();
            }

            @Override
            public void deductStock(List<OrderDetailDTO> items) {
                log.error("减少商品库存失败！:",cause);
                // 库存扣减业务需要触发事务回滚，查询失败，抛出异常
                throw new RuntimeException(cause);
            }
        };
    }
}
