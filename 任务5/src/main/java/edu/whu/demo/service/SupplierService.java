package edu.whu.demo.service;

import edu.whu.demo.entity.Supplier;
import edu.whu.demo.dao.SupplierDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupplierService {
    @Autowired
    private SupplierDao supplierMapper;

    // 添加供货商
    public Supplier addSupplier(Supplier supplier) {
        supplierMapper.insert(supplier);
        return supplier;
    }

    // 根据ID查询供货商
    public Supplier getSupplierById(Long id) {
        return supplierMapper.selectById(id);
    }

    // 查询所有供货商
    public List<Supplier> getAllSuppliers() {
        return supplierMapper.selectList(null);
    }

    // 更新供货商信息
    public Supplier updateSupplier(Long id, Supplier updatedSupplier) {
        updatedSupplier.setId(id);
        supplierMapper.updateById(updatedSupplier);
        return updatedSupplier;
    }

    // 删除供货商
    public void deleteSupplier(Long id) {
        supplierMapper.deleteById(id);
    }
}
