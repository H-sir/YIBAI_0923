/*
 * Copyright (c) 2020/1/7. by Onlly
 * 个人简介：野生程序员
 * QQ:412102100
 * WX:i412102100
 * Github: Github.com/mingyouzhu
 * 简书: https://www.jianshu.com/u/4198eba0fbdd
 */
package com.ybw.yibai.common.permission;

/**
 * Enum class to handle the different states
 * of permissions since the PackageManager only
 * has a granted and denied state.
 */
enum Permissions {
  GRANTED,
  DENIED,
  NOT_FOUND
}