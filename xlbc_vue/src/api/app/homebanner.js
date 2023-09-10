import request from '@/utils/request'

// 查询轮播图列表
export function listHomebanner(query) {
  return request({
    url: '/app/homebanner/list',
    method: 'get',
    params: query
  })
}

// 查询轮播图详细
export function getHomebanner(id) {
  return request({
    url: '/app/homebanner/' + id,
    method: 'get'
  })
}

// 新增轮播图
export function addHomebanner(data) {
  return request({
    url: '/app/homebanner',
    method: 'post',
    data: data
  })
}

// 修改轮播图
export function updateHomebanner(data) {
  return request({
    url: '/app/homebanner',
    method: 'put',
    data: data
  })
}

// 删除轮播图
export function delHomebanner(id) {
  return request({
    url: '/app/homebanner/' + id,
    method: 'delete'
  })
}
