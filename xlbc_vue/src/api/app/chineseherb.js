import request from '@/utils/request'

// 查询中药数据列表
export function listChineseherb(query) {
  return request({
    url: '/app/chineseherb/list',
    method: 'get',
    params: query
  })
}

// 查询中药数据详细
export function getChineseherb(id) {
  return request({
    url: '/app/chineseherb/' + id,
    method: 'get'
  })
}

// 新增中药数据
export function addChineseherb(data) {
  return request({
    url: '/app/chineseherb',
    method: 'post',
    data: data
  })
}

// 修改中药数据
export function updateChineseherb(data) {
  return request({
    url: '/app/chineseherb',
    method: 'put',
    data: data
  })
}

// 删除中药数据
export function delChineseherb(id) {
  return request({
    url: '/app/chineseherb/' + id,
    method: 'delete'
  })
}
