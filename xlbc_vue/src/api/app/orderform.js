import request from '@/utils/request'

// 查询订单管理列表
export function listOrderform(query) {
  return request({
    url: '/app/orderform/list',
    method: 'get',
    params: query
  })
}

// 查询订单管理详细
export function getOrderform(id) {
  return request({
    url: '/app/orderform/' + id,
    method: 'get'
  })
}

// 新增订单管理
export function addOrderform(data) {
  return request({
    url: '/app/orderform',
    method: 'post',
    data: data
  })
}

// 修改订单管理
export function updateOrderform(id) {
  return request({
    url: '/app/orderform/' + id,
    method: 'put',
  })
}

// 删除订单管理
export function delOrderform(id) {
  return request({
    url: '/app/orderform/' + id,
    method: 'delete'
  })
}
