import request from '@/utils/request'

// 查询药膳数据列表
export function listMedicateddiet(query) {
  return request({
    url: '/app/medicateddiet/list',
    method: 'get',
    params: query
  })
}

// 查询药膳数据详细
export function getMedicateddiet(id) {
  return request({
    url: '/app/medicateddiet/' + id,
    method: 'get'
  })
}

// 新增药膳数据
export function addMedicateddiet(data) {
  return request({
    url: '/app/medicateddiet',
    method: 'post',
    data: data
  })
}

// 修改药膳数据
export function updateMedicateddiet(data) {
  return request({
    url: '/app/medicateddiet',
    method: 'put',
    data: data
  })
}

// 删除药膳数据
export function delMedicateddiet(id) {
  return request({
    url: '/app/medicateddiet/' + id,
    method: 'delete'
  })
}
