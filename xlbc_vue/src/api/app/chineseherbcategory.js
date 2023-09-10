import request from '@/utils/request'

// 查询中药分类列表
export function listChineseherbcategory(query) {
  return request({
    url: '/app/chineseherbcategory/list',
    method: 'get',
    params: query
  })
}

export function listChineseherbcategoryByParent(parentId) {
  return request({
    url: '/app/chineseherbcategory/listByParent/' + parentId,
    method: 'get'
  })
}

// 查询中药分类详细
export function getChineseherbcategory(categoryId) {
  return request({
    url: '/app/chineseherbcategory/' + categoryId,
    method: 'get'
  })
}

// 新增中药分类
export function addChineseherbcategory(data) {
  return request({
    url: '/app/chineseherbcategory',
    method: 'post',
    data: data
  })
}

// 修改中药分类
export function updateChineseherbcategory(data) {
  return request({
    url: '/app/chineseherbcategory',
    method: 'put',
    data: data
  })
}

// 删除中药分类
export function delChineseherbcategory(categoryId) {
  return request({
    url: '/app/chineseherbcategory/' + categoryId,
    method: 'delete'
  })
}
