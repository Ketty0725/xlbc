import request from '@/utils/request'

// 查询药品分类列表
export function listProductcategory(query) {
  return request({
    url: '/app/productcategory/list',
    method: 'get',
    params: query
  })
}

// 查询药品分类详细
export function getProductcategory(categoryId) {
  return request({
    url: '/app/productcategory/' + categoryId,
    method: 'get'
  })
}

// 新增药品分类
export function addProductcategory(data) {
  return request({
    url: '/app/productcategory',
    method: 'post',
    data: data
  })
}

// 修改药品分类
export function updateProductcategory(data) {
  return request({
    url: '/app/productcategory',
    method: 'put',
    data: data
  })
}

// 删除药品分类
export function delProductcategory(categoryId) {
  return request({
    url: '/app/productcategory/' + categoryId,
    method: 'delete'
  })
}
