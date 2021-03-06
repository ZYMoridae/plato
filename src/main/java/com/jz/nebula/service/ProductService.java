package com.jz.nebula.service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import com.google.common.base.Strings;
import com.jz.nebula.util.auth.AuthenticationFacadeImpl;
import com.jz.nebula.entity.sku.Sku;
import com.jz.nebula.entity.sku.SkuAttribute;
import com.jz.nebula.util.pagination.CmsPagination;
import com.jz.nebula.util.pagination.CmsPaginationHelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import com.jz.nebula.controller.api.ProductController;
import com.jz.nebula.dao.ProductRepository;
import com.jz.nebula.entity.product.Product;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private AuthenticationFacadeImpl authenticationFacadeImpl;

    @Autowired
    private SkuService skuService;

    /**
     * Find all based on keyword and pagination parameter
     *
     * @param keyword
     * @param pageable
     * @param assembler
     *
     * @return
     */
    public PagedModel<EntityModel<Product>> findAll(String keyword, Pageable pageable,
                                                    PagedResourcesAssembler<Product> assembler) {
        Page<Product> page;
        if (Strings.isNullOrEmpty(keyword)) {
            page = productRepository.findAllByOrderByIdAsc(pageable);
        } else {
            page = productRepository.findByNameContaining(keyword, pageable);
        }

        PagedModel<EntityModel<Product>> resources = assembler.toModel(page,
                linkTo(ProductController.class).slash("/products").withSelfRel());

        return resources;
    }

    /**
     * Find all products by pageable
     *
     * @param pageable
     *
     * @return
     */
    public CmsPagination findAll(Pageable pageable) {
        CmsPaginationHelper<Product> cmsPaginationHelper = new CmsPaginationHelper<>();
        Page<Product> pageProduct = productRepository.findAllByOrderByIdAsc(pageable);
        return cmsPaginationHelper.getCmsPagination(pageable, pageProduct, "/cms/product");
    }

    /**
     * NOTE: You can not get nested objects from findBy function because the object was not persisted
     *
     * @param product
     *
     * @return
     */
    @Transactional(rollbackFor = {Exception.class})
    public synchronized Product save(Product product) {
        Set<Sku> skuList = product.getSkus();

        Product updatedProduct;

        if (product.getId() != null) {
            skuList.stream().forEach(sku -> {
//                if (sku.getId() == null) {
                sku.setProductId(product.getId());
                sku.setCreatedUserId(product.getVendor().getId());
                bulkSaveSkuAttributes(sku);
//                }
            });

            updatedProduct = productRepository.save(product);
        } else {
            product.setSkus(new HashSet());

            updatedProduct = productRepository.save(product);

            skuList.stream().forEach(sku -> {
                sku.setProductId(updatedProduct.getId());
                if (Objects.isNull(updatedProduct.getVendor().getId())) {
                    sku.setCreatedUserId(authenticationFacadeImpl.getUserId());
                } else {
                    sku.setCreatedUserId(updatedProduct.getVendor().getId());
                }

                bulkSaveSkuAttributes(sku);
            });
        }

        return updatedProduct;
    }

    /**
     * Bulk save
     *
     * @param sku
     */
//    @Transactional(rollbackFor = {Exception.class})
    public void bulkSaveSkuAttributes(Sku sku) {
        Set<SkuAttribute> skuAttributes = sku.getSkuAttributes();
        sku.setSkuAttributes(new HashSet<>());

        Sku updatedSku = skuService.create(sku);
        skuAttributes.stream().forEach(skuAttribute -> {
//            if (sku.getId() == null) {
            skuAttribute.setSkuCode(updatedSku.getSkuCode());
//            }
//            skuAttribute.setSkuCode(updatedSku.getSkuCode());
            skuService.createSkuAttribute(skuAttribute);
        });
    }

    public Product findById(long id) {
        return productRepository.findById(id).get();
    }

    public void delete(long id) {
        productRepository.deleteById(id);
    }

    public List<Product> findByIds(List<Long> ids) {
        return productRepository.findByIdIn(ids);
    }

    /**
     * Validate product object
     *
     * @param product
     *
     * @return
     */
    private boolean isValidProduct(Product product) {
        boolean isValid = true;

        return true;
    }
}
