package com.core.identity.exception;

public class RefreshTokenExpiredException extends BaseException {

  public RefreshTokenExpiredException() {
    super("Refresh tokenin vaxtı bitib. Zəhmət olmasa yenidən daxil olun", 401);
  }
}
