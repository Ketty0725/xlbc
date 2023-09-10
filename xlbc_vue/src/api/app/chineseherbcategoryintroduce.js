import request from '@/utils/request'

// 查询分类介绍列表
export function listChineseherbcategoryintroduce(query) {
  return request({
    url: '/app/chineseherbcategoryintroduce/list',
    method: 'get',
    params: query
  })
}

// 查询分类介绍详细
export function getChineseherbcategoryintroduce(id) {
  return request({
    url: '/app/chineseherbcategoryintroduce/' + id,
    method: 'get'
  })
}

// 新增分类介绍
export function addChineseherbcategoryintroduce(data) {
  return request({
    url: '/app/chineseherbcategoryintroduce',
    method: 'post',
    data: data
  })
}

// 修改分类介绍
export function updateChineseherbcategoryintroduce(data) {
  return request({
    url: '/app/chineseherbcategoryintroduce',
    method: 'put',
    data: data
  })
}

// 删除分类介绍
export function delChineseherbcategoryintroduce(id) {
  return request({
    url: '/app/chineseherbcategoryintroduce/' + id,
    method: 'delete'
  })
}
