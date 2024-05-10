package hhplus.serverjava.api.util.aop;

public enum LockType {
	USER_POINT {
		@Override
		public String getKey(Long userId) {
			return "USER_POINT:".concat(userId.toString());
		}
	};

	public abstract String getKey(Long userId);
}
