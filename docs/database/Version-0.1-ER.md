# Version 0.1：数据库设计

## 目标

Version 0.1 的数据库只支撑最小业务闭环：

```text
注册 → 登录 → 查看题目 → 提交 Flag → 排行榜变化
```

本版本先设计 4 张核心表：

1. `users`
2. `challenges`
3. `submissions`
4. `solves`

## ER 关系

```text
users 1 ── n submissions
users 1 ── n solves

challenges 1 ── n submissions
challenges 1 ── n solves

users n ── n challenges，通过 solves 表表示已解出的题目
```

## users

用户表。

### 字段

| 字段 | 类型 | 约束 | 说明 |
| --- | --- | --- | --- |
| `id` | `bigserial` | 主键 | 用户 ID |
| `username` | `varchar(32)` | 非空，唯一 | 用户名 |
| `email` | `varchar(255)` | 非空，唯一 | 邮箱 |
| `password_hash` | `varchar(255)` | 非空 | 密码哈希 |
| `role` | `varchar(32)` | 非空 | 用户角色 |
| `status` | `varchar(32)` | 非空 | 用户状态 |
| `created_at` | `timestamptz` | 非空 | 创建时间 |
| `updated_at` | `timestamptz` | 非空 | 更新时间 |

### 约束

1. `username` 唯一。
2. `email` 唯一。
3. `role` 只能是 `USER` 或 `ADMIN`。
4. `status` 只能是 `ACTIVE` 或 `DISABLED`。

## challenges

题目表。

### 字段

| 字段 | 类型 | 约束 | 说明 |
| --- | --- | --- | --- |
| `id` | `bigserial` | 主键 | 题目 ID |
| `title` | `varchar(128)` | 非空 | 题目标题 |
| `description` | `text` | 非空 | 题目描述 |
| `category` | `varchar(64)` | 非空 | 题目分类 |
| `score` | `integer` | 非空 | 题目分值 |
| `flag_hash` | `varchar(255)` | 非空 | Flag 哈希 |
| `status` | `varchar(32)` | 非空 | 题目状态 |
| `created_at` | `timestamptz` | 非空 | 创建时间 |
| `updated_at` | `timestamptz` | 非空 | 更新时间 |

### 约束

1. `score` 必须大于 0。
2. `status` 只能是 `ONLINE` 或 `OFFLINE`。
3. `flag_hash` 不能返回给前端。

## submissions

提交记录表。

### 字段

| 字段 | 类型 | 约束 | 说明 |
| --- | --- | --- | --- |
| `id` | `bigserial` | 主键 | 提交记录 ID |
| `user_id` | `bigint` | 非空，外键 | 用户 ID |
| `challenge_id` | `bigint` | 非空，外键 | 题目 ID |
| `submitted_flag_hash` | `varchar(128)` | 非空 | 用户提交内容的摘要 |
| `result` | `varchar(32)` | 非空 | 提交结果 |
| `created_at` | `timestamptz` | 非空 | 提交时间 |

### 约束

1. `user_id` 引用 `users.id`。
2. `challenge_id` 引用 `challenges.id`。
3. `result` 只能是 `ACCEPTED`、`WRONG_ANSWER`、`ALREADY_SOLVED`。
4. 不保存用户提交的原始 Flag。

### 索引

1. `idx_submissions_user_id_created_at`
2. `idx_submissions_challenge_id_created_at`
3. `idx_submissions_user_challenge_created_at`

## solves

解题记录表。

### 字段

| 字段 | 类型 | 约束 | 说明 |
| --- | --- | --- | --- |
| `id` | `bigserial` | 主键 | 解题记录 ID |
| `user_id` | `bigint` | 非空，外键 | 用户 ID |
| `challenge_id` | `bigint` | 非空，外键 | 题目 ID |
| `score` | `integer` | 非空 | 本次解题获得的分数 |
| `solved_at` | `timestamptz` | 非空 | 解题时间 |

### 约束

1. `user_id` 引用 `users.id`。
2. `challenge_id` 引用 `challenges.id`。
3. `score` 必须大于 0。
4. `user_id` 和 `challenge_id` 组合唯一，保证同一用户同一道题只能加一次分。

### 索引

1. `uk_solves_user_challenge`
2. `idx_solves_user_id`
3. `idx_solves_challenge_id`
4. `idx_solves_solved_at`

## PostgreSQL 建表草案

```sql
create table users (
    id bigserial primary key,
    username varchar(32) not null unique,
    email varchar(255) not null unique,
    password_hash varchar(255) not null,
    role varchar(32) not null,
    status varchar(32) not null,
    created_at timestamptz not null,
    updated_at timestamptz not null,
    constraint ck_users_role check (role in ('USER', 'ADMIN')),
    constraint ck_users_status check (status in ('ACTIVE', 'DISABLED'))
);

create table challenges (
    id bigserial primary key,
    title varchar(128) not null,
    description text not null,
    category varchar(64) not null,
    score integer not null,
    flag_hash varchar(255) not null,
    status varchar(32) not null,
    created_at timestamptz not null,
    updated_at timestamptz not null,
    constraint ck_challenges_score check (score > 0),
    constraint ck_challenges_status check (status in ('ONLINE', 'OFFLINE'))
);

create table submissions (
    id bigserial primary key,
    user_id bigint not null references users(id),
    challenge_id bigint not null references challenges(id),
    submitted_flag_hash varchar(128) not null,
    result varchar(32) not null,
    created_at timestamptz not null,
    constraint ck_submissions_result check (result in ('ACCEPTED', 'WRONG_ANSWER', 'ALREADY_SOLVED'))
);

create table solves (
    id bigserial primary key,
    user_id bigint not null references users(id),
    challenge_id bigint not null references challenges(id),
    score integer not null,
    solved_at timestamptz not null,
    constraint ck_solves_score check (score > 0),
    constraint uk_solves_user_challenge unique (user_id, challenge_id)
);

create index idx_submissions_user_id_created_at on submissions(user_id, created_at desc);
create index idx_submissions_challenge_id_created_at on submissions(challenge_id, created_at desc);
create index idx_submissions_user_challenge_created_at on submissions(user_id, challenge_id, created_at desc);

create index idx_solves_user_id on solves(user_id);
create index idx_solves_challenge_id on solves(challenge_id);
create index idx_solves_solved_at on solves(solved_at);
```

## 排行榜查询草案

```sql
select
    u.id as user_id,
    u.username,
    coalesce(sum(s.score), 0) as score,
    count(s.id) as solved_count,
    max(s.solved_at) as last_solved_at
from users u
join solves s on s.user_id = u.id
where u.status = 'ACTIVE'
group by u.id, u.username
order by score desc, last_solved_at asc, u.id asc;
```

## 题目列表查询需要的数据

题目列表需要同时返回解题人数和当前用户是否已解出。

后端可以通过 SQL 聚合，也可以分多次查询后在 Service 层组装。

Version 0.1 优先选择代码可读性，不必过早追求单条复杂 SQL。

## 数据库设计原则

1. 业务唯一性必须有数据库约束。
2. 密码和 Flag 不保存明文。
3. 所有时间字段使用 `timestamptz`。
4. 枚举值先用 `varchar` 保存，方便 Java 枚举映射。
5. Version 0.1 不做软删除。
6. 后续有迁移工具后，再使用 Flyway 或 Liquibase 管理建表脚本。

