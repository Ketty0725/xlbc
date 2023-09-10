import request from '@/utils/request'

// 查询方剂数据列表
export function listPrescription(query) {
  return request({
    url: '/app/prescription/list',
    method: 'get',
    params: query
  })
}

// 查询方剂数据详细
export function getPrescription(id) {
  return request({
    url: '/app/prescription/' + id,
    method: 'get'
  })
}

// 新增方剂数据
export function addPrescription(data) {
  return request({
    url: '/app/prescription',
    method: 'post',
    data: data
  })
}

// 修改方剂数据
export function updatePrescription(data) {
  return request({
    url: '/app/prescription',
    method: 'put',
    data: data
  })
}

// 删除方剂数据
export function delPrescription(id) {
  return request({
    url: '/app/prescription/' + id,
    method: 'delete'
  })
}
