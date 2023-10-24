package edu.whu.demo.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.whu.demo.entity.Product;
import org.springframework.stereotype.Repository;

@Repository
public interface ProuductDao extends BaseMapper<Product> {

}
