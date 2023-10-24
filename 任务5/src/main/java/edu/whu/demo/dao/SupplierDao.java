package edu.whu.demo.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.whu.demo.entity.Supplier;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierDao extends BaseMapper<Supplier> {
}
