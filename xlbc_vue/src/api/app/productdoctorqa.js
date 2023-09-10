import request from '@/utils/request'

// 查询医生问答列表
export function listProductdoctorqa(query) {
  return request({
    url: '/app/productdoctorqa/list',
    method: 'get',
    params: query
  })
}

// 查询医生问答详细
export function getProductdoctorqa(id) {
  return request({
    url: '/app/productdoctorqa/' + id,
    method: 'get'
  })
}

// 新增医生问答
export function addProductdoctorqa(data) {
  return request({
    url: '/app/productdoctorqa',
    method: 'post',
    data: data
  })
}

// 修改医生问答
export function updateProductdoctorqa(data) {
  return request({
    url: '/app/productdoctorqa',
    method: 'put',
    data: data
  })
}

// 删除医生问答
export function delProductdoctorqa(id) {
  return request({
    url: '/app/productdoctorqa/' + id,
    method: 'delete'
  })
}
