import request from '@/utils/request'

// 查询药品说明列表
export function listProductbasicinfo(query) {
  return request({
    url: '/app/productbasicinfo/list',
    method: 'get',
    params: query
  })
}

// 查询药品说明详细
export function getProductbasicinfo(id) {
  return request({
    url: '/app/productbasicinfo/' + id,
    method: 'get'
  })
}

// 新增药品说明
export function addProductbasicinfo(data) {
  return request({
    url: '/app/productbasicinfo',
    method: 'post',
    data: data
  })
}

// 修改药品说明
export function updateProductbasicinfo(data) {
  return request({
    url: '/app/productbasicinfo',
    method: 'put',
    data: data
  })
}

// 删除药品说明
export function delProductbasicinfo(id) {
  return request({
    url: '/app/productbasicinfo/' + id,
    method: 'delete'
  })
}
