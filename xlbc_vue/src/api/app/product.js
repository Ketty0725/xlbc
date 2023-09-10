import request from '@/utils/request'

export function listAllProduct() {
  return request({
    url: '/app/product/listAll',
    method: 'get'
  })
}

// 查询药品信息列表
export function listProduct(query) {
  return request({
    url: '/app/product/list',
    method: 'get',
    params: query
  })
}

// 查询药品信息详细
export function getProduct(id) {
  return request({
    url: '/app/product/' + id,
    method: 'get'
  })
}

// 新增药品信息
export function addProduct(data) {
  return request({
    url: '/app/product',
    method: 'post',
    data: data
  })
}

// 修改药品信息
export function updateProduct(data) {
  return request({
    url: '/app/product',
    method: 'put',
    data: data
  })
}

// 删除药品信息
export function delProduct(id) {
  return request({
    url: '/app/product/' + id,
    method: 'delete'
  })
}
