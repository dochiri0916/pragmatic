# Pragmatic Architecture

회원가입/로그인/JWT 재발급/로그아웃 흐름에서, 구조 복잡도를 과하게 높이지 않으면서 실무적으로 유지 가능한 계층 분리와 보안 처리 방식을 검증한 프로젝트다.
여기에는 실행 가능한 코드와 핵심 요약만 두고, 설계 배경과 선택 이유는 별도 글로 정리했다.

## 실험 범위

- `User`/`RefreshToken` 중심 인증 도메인을 기준으로 API 흐름 구성
- 로그인 시 Access Token 발급 + Refresh Token 쿠키 저장
- 재발급 시 Refresh Token Rotation 적용으로 탈취 토큰 재사용 위험 완화
- 예외를 도메인/인증 유형별로 매핑해 일관된 API 에러 응답 제공
- `application/domain/infrastructure/presentation` 계층 구조와 의존성 규칙을 테스트로 검증

## 자세한 내용

[사용할지도 모르는데 선불권부터 끊어둔 설계에 대하여](https://velog.io/@dochiri0916/%EC%82%AC%EC%9A%A9%ED%95%A0%EC%A7%80%EB%8F%84-%EB%AA%A8%EB%A5%B4%EB%8A%94%EB%8D%B0-%EC%84%A0%EB%B6%88%EA%B6%8C%EB%B6%80%ED%84%B0-%EB%81%8A%EC%96%B4%EB%91%94-%EC%84%A4%EA%B3%84%EC%97%90-%EB%8C%80%ED%95%98%EC%97%AC)