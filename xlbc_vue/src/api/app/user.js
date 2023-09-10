import request from '@/utils/request'

// 查询用户列表
export function listUser(query) {
  return request({
    url: '/app/user/list',
    method: 'get',
    params: query
  })
}

// 查询用户详细
export function getUser(uId) {
  return request({
    url: '/app/user/' + uId,
    method: 'get'
  })
}

// 删除用户
export function delUser(uId) {
  return request({
    url: '/app/user/' + uId,
    method: 'delete'
  })
}
