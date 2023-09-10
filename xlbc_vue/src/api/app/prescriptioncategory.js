import request from '@/utils/request'

// 查询方剂分类列表
export function listPrescriptioncategory(query) {
  return request({
    url: '/app/prescriptioncategory/list',
    method: 'get',
    params: query
  })
}

export function listPrescriptioncategoryByParent(parentId) {
  return request({
    url: '/app/prescriptioncategory/listByParent/' + parentId,
    method: 'get'
  })
}

// 查询方剂分类详细
export function getPrescriptioncategory(categoryId) {
  return request({
    url: '/app/prescriptioncategory/' + categoryId,
    method: 'get'
  })
}

// 新增方剂分类
export function addPrescriptioncategory(data) {
  return request({
    url: '/app/prescriptioncategory',
    method: 'post',
    data: data
  })
}

// 修改方剂分类
export function updatePrescriptioncategory(data) {
  return request({
    url: '/app/prescriptioncategory',
    method: 'put',
    data: data
  })
}

// 删除方剂分类
export function delPrescriptioncategory(categoryId) {
  return request({
    url: '/app/prescriptioncategory/' + categoryId,
    method: 'delete'
  })
}
