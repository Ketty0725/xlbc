import request from '@/utils/request'

// 查询笔记列表
export function listCommunity(query) {
  return request({
    url: '/app/community/list',
    method: 'get',
    params: query
  })
}

// 查询笔记详细
export function getCommunity(id) {
  return request({
    url: '/app/community/' + id,
    method: 'get'
  })
}

// 新增笔记
export function addCommunity(data) {
  return request({
    url: '/app/community',
    method: 'post',
    data: data
  })
}

// 修改笔记
export function updateCommunity(data) {
  return request({
    url: '/app/community',
    method: 'put',
    data: data
  })
}

// 删除笔记
export function delCommunity(id) {
  return request({
    url: '/app/community/' + id,
    method: 'delete'
  })
}
