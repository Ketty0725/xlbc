import request from '@/utils/request'

// 查询中成药数据列表
export function listChinesepatentmedicine(query) {
  return request({
    url: '/app/chinesepatentmedicine/list',
    method: 'get',
    params: query
  })
}

// 查询中成药数据详细
export function getChinesepatentmedicine(id) {
  return request({
    url: '/app/chinesepatentmedicine/' + id,
    method: 'get'
  })
}

// 新增中成药数据
export function addChinesepatentmedicine(data) {
  return request({
    url: '/app/chinesepatentmedicine',
    method: 'post',
    data: data
  })
}

// 修改中成药数据
export function updateChinesepatentmedicine(data) {
  return request({
    url: '/app/chinesepatentmedicine',
    method: 'put',
    data: data
  })
}

// 删除中成药数据
export function delChinesepatentmedicine(id) {
  return request({
    url: '/app/chinesepatentmedicine/' + id,
    method: 'delete'
  })
}
