import request from '@/utils/request'

// 查询经方列表
export function listTyphoidtheoryprescription(query) {
  return request({
    url: '/app/typhoidtheoryprescription/list',
    method: 'get',
    params: query
  })
}

// 查询经方详细
export function getTyphoidtheoryprescription(id) {
  return request({
    url: '/app/typhoidtheoryprescription/' + id,
    method: 'get'
  })
}

// 新增经方
export function addTyphoidtheoryprescription(data) {
  return request({
    url: '/app/typhoidtheoryprescription',
    method: 'post',
    data: data
  })
}

// 修改经方
export function updateTyphoidtheoryprescription(data) {
  return request({
    url: '/app/typhoidtheoryprescription',
    method: 'put',
    data: data
  })
}

// 删除经方
export function delTyphoidtheoryprescription(id) {
  return request({
    url: '/app/typhoidtheoryprescription/' + id,
    method: 'delete'
  })
}
