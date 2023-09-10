import request from '@/utils/request'

// 查询中药列表
export function listTyphoidtheorychineseherb(query) {
  return request({
    url: '/app/typhoidtheorychineseherb/list',
    method: 'get',
    params: query
  })
}

// 查询中药详细
export function getTyphoidtheorychineseherb(id) {
  return request({
    url: '/app/typhoidtheorychineseherb/' + id,
    method: 'get'
  })
}

// 新增中药
export function addTyphoidtheorychineseherb(data) {
  return request({
    url: '/app/typhoidtheorychineseherb',
    method: 'post',
    data: data
  })
}

// 修改中药
export function updateTyphoidtheorychineseherb(data) {
  return request({
    url: '/app/typhoidtheorychineseherb',
    method: 'put',
    data: data
  })
}

// 删除中药
export function delTyphoidtheorychineseherb(id) {
  return request({
    url: '/app/typhoidtheorychineseherb/' + id,
    method: 'delete'
  })
}
