# Version 0.1：API 设计

## 基本约定

Version 0.1 的 API 统一使用 JSON。

基础路径：

```text
/api
```

时间格式统一使用 ISO-8601 字符串。

示例：

```text
2026-06-26T12:00:00+08:00
```

## 认证方式

除注册和登录外，其他接口都需要携带 JWT。

请求头：

```text
Authorization: Bearer <token>
```

## 通用响应格式

成功响应：

```json
{
  "code": "OK",
  "message": "成功",
  "data": {}
}
```

失败响应：

```json
{
  "code": "VALIDATION_ERROR",
  "message": "请求参数不合法",
  "data": null
}
```

## 通用错误码

| 错误码 | HTTP 状态 | 说明 |
| --- | --- | --- |
| `OK` | 200 | 成功 |
| `VALIDATION_ERROR` | 400 | 请求参数不合法 |
| `UNAUTHORIZED` | 401 | 未登录 |
| `INVALID_TOKEN` | 401 | Token 无效或过期 |
| `FORBIDDEN` | 403 | 无权限 |
| `NOT_FOUND` | 404 | 资源不存在 |
| `CONFLICT` | 409 | 资源冲突 |
| `INTERNAL_ERROR` | 500 | 系统异常 |

## 认证接口

### 注册

```text
POST /api/auth/register
```

是否需要登录：否。

请求体：

```json
{
  "username": "alice",
  "email": "alice@example.com",
  "password": "password123"
}
```

字段规则：

| 字段 | 规则 |
| --- | --- |
| `username` | 必填，3 到 32 位，只允许字母、数字、下划线和短横线 |
| `email` | 必填，合法邮箱 |
| `password` | 必填，至少 8 位 |

成功响应：

```json
{
  "code": "OK",
  "message": "成功",
  "data": {
    "id": 1,
    "username": "alice",
    "email": "alice@example.com"
  }
}
```

可能错误：

| 错误码 | HTTP 状态 | 说明 |
| --- | --- | --- |
| `VALIDATION_ERROR` | 400 | 参数不合法 |
| `USERNAME_ALREADY_EXISTS` | 409 | 用户名已存在 |
| `EMAIL_ALREADY_EXISTS` | 409 | 邮箱已存在 |

### 登录

```text
POST /api/auth/login
```

是否需要登录：否。

请求体：

```json
{
  "account": "alice",
  "password": "password123"
}
```

说明：

`account` 可以是用户名，也可以是邮箱。

成功响应：

```json
{
  "code": "OK",
  "message": "成功",
  "data": {
    "token": "jwt-token",
    "token_type": "Bearer",
    "expires_in": 86400,
    "user": {
      "id": 1,
      "username": "alice",
      "email": "alice@example.com",
      "role": "USER"
    }
  }
}
```

可能错误：

| 错误码 | HTTP 状态 | 说明 |
| --- | --- | --- |
| `VALIDATION_ERROR` | 400 | 参数不合法 |
| `INVALID_CREDENTIALS` | 401 | 账号或密码错误 |
| `USER_DISABLED` | 403 | 用户被禁用 |

## 题目接口

### 获取题目列表

```text
GET /api/challenges
```

是否需要登录：是。

查询参数：

| 参数 | 必填 | 说明 |
| --- | --- | --- |
| `category` | 否 | 按分类过滤 |

成功响应：

```json
{
  "code": "OK",
  "message": "成功",
  "data": [
    {
      "id": 1,
      "title": "Hello Web",
      "category": "Web",
      "score": 100,
      "solved_count": 12,
      "solved_by_me": false
    }
  ]
}
```

说明：

1. 只返回 `ONLINE` 题目。
2. 不返回题目描述。
3. 不返回 Flag 相关字段。

### 获取题目详情

```text
GET /api/challenges/{id}
```

是否需要登录：是。

路径参数：

| 参数 | 说明 |
| --- | --- |
| `id` | 题目 ID |

成功响应：

```json
{
  "code": "OK",
  "message": "成功",
  "data": {
    "id": 1,
    "title": "Hello Web",
    "description": "找到页面中的 flag。",
    "category": "Web",
    "score": 100,
    "solved_count": 12,
    "solved_by_me": false
  }
}
```

可能错误：

| 错误码 | HTTP 状态 | 说明 |
| --- | --- | --- |
| `CHALLENGE_NOT_FOUND` | 404 | 题目不存在或已下线 |

## 提交接口

### 提交 Flag

```text
POST /api/challenges/{id}/submissions
```

是否需要登录：是。

路径参数：

| 参数 | 说明 |
| --- | --- |
| `id` | 题目 ID |

请求体：

```json
{
  "flag": "flag{example}"
}
```

成功响应：Flag 正确。

```json
{
  "code": "OK",
  "message": "成功",
  "data": {
    "result": "ACCEPTED",
    "correct": true,
    "already_solved": false,
    "score_added": 100
  }
}
```

成功响应：Flag 错误。

```json
{
  "code": "OK",
  "message": "成功",
  "data": {
    "result": "WRONG_ANSWER",
    "correct": false,
    "already_solved": false,
    "score_added": 0
  }
}
```

成功响应：已经解出。

```json
{
  "code": "OK",
  "message": "成功",
  "data": {
    "result": "ALREADY_SOLVED",
    "correct": true,
    "already_solved": true,
    "score_added": 0
  }
}
```

可能错误：

| 错误码 | HTTP 状态 | 说明 |
| --- | --- | --- |
| `VALIDATION_ERROR` | 400 | Flag 为空 |
| `CHALLENGE_NOT_FOUND` | 404 | 题目不存在或已下线 |
| `SUBMISSION_FAILED` | 500 | 判题异常 |

## 排行榜接口

### 获取排行榜

```text
GET /api/rankings
```

是否需要登录：是。

查询参数：

| 参数 | 必填 | 默认值 | 说明 |
| --- | --- | --- | --- |
| `limit` | 否 | 50 | 返回数量 |

成功响应：

```json
{
  "code": "OK",
  "message": "成功",
  "data": [
    {
      "rank": 1,
      "user_id": 1,
      "username": "alice",
      "score": 300,
      "solved_count": 3,
      "last_solved_at": "2026-06-26T12:00:00+08:00"
    }
  ]
}
```

排序规则：

1. 总分从高到低。
2. 总分相同时，最后一次正确提交时间从早到晚。
3. 仍然相同时，用户 ID 从小到大。

## 前端页面建议

Version 0.1 前端至少需要以下页面。

1. 注册页
2. 登录页
3. 题目列表页
4. 题目详情页
5. 排行榜页

## 后端模块建议

Version 0.1 后端至少需要以下模块。

1. `auth`：注册、登录、JWT
2. `user`：用户实体和当前用户识别
3. `challenge`：题目列表和题目详情
4. `submission`：提交 Flag 和判题
5. `ranking`：排行榜
6. `common`：统一响应、异常处理、错误码

