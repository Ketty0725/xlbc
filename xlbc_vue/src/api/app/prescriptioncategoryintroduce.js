import request from '@/utils/request'

// 查询分类介绍列表
export function listPrescriptioncategoryintroduce(query) {
  return request({
    url: '/app/prescriptioncategoryintroduce/list',
    method: 'get',
    params: query
  })
}

// 查询分类介绍详细
export function getPrescriptioncategoryintroduce(id) {
  return request({
    url: '/app/prescriptioncategoryintroduce/' + id,
    method: 'get'
  })
}

// 新增分类介绍
export function addPrescriptioncategoryintroduce(data) {
  return request({
    url: '/app/prescriptioncategoryintroduce',
    method: 'post',
    data: data
  })
}

// 修改分类介绍
export function updatePrescriptioncategoryintroduce(data) {
  return request({
    url: '/app/prescriptioncategoryintroduce',
    method: 'put',
    data: data
  })
}

// 删除分类介绍
export function delPrescriptioncategoryintroduce(id) {
  return request({
    url: '/app/prescriptioncategoryintroduce/' + id,
    method: 'delete'
  })
}
