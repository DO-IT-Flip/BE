
# 📅 Flip
> A simple and aesthetic scheduling service, "Flip".


<br/>
<br/>
<br/>


## 🗂️ 목차

1. [프로젝트 소개](#프로젝트-소개)  
2. [기술 스택](#기술-스택)  
3. [시스템 아키텍처](#시스템-아키텍처)  
4. [주요 기능](#주요-기능)  
5. [인증 시스템](#인증-시스템)  
6. [CI/CD 및 배포](#cicd-및-배포)  
7. [Git 전략 및 협업 관리](#git-전략-및-협업-관리)  
8. [ERD 및 DB 설계](#erd-및-db-설계)  
9. [실행 방법](#실행-방법)  
10. [트러블슈팅](#트러블슈팅)  
11. [향후 개선 사항](#향후-개선-사항)  
12. [개발자](#개발자)


<br/>
<br/>
<br/>


## 📌 프로젝트 소개

**Flip**는 개인 일정 및 태그 기반 캘린더를 제공하는 서비스입니다.  
단순한 일정 CRUD를 넘어서, 백엔드 실무를 체험하기 위한 프로젝트로, 인증부터 배포까지 전 과정을 직접 구현했습니다.


<br/>
<br/>
<br/>


## 🛠 기술 스택

| 분야         | 기술 스택                                      |
|--------------|-----------------------------------------------|
| Language     | Java 21                                       |
| Framework    | Spring Boot 3.x, Spring Security, JPA         |
| DB           | PostgreSQL (Supabase)                         |
| Cache        | Redis (Docker 컨테이너)                       |
| Auth         | JWT (Access + Refresh)                        |
| Infra        | Docker, GCP Cloud Run                         |
| DevOps       | GitHub Actions, .env 환경 변수 관리           |
| Docs         | Swagger UI, springdoc-openapi                 |
| 기타         | Git Flow 전략, Gitmoji 커밋, Issue & PR 리뷰 관리 |


<br/>
<br/>
<br/>


## 🏗 시스템 아키텍처
```

[Client]
   ↓
[Spring Boot 앱] ⇄ [Redis (Refresh Token)]
   ↓
[PostgreSQL (Supabase)]
        ↓
[Docker 이미지] → [GCP Cloud Run에 배포]
```

- Supabase: 외부 클라우드 DB  
- Redis: JWT Refresh Token 저장 (Docker 컨테이너 내)  
- Docker: Spring Boot + Redis를 하나의 컨테이너로 구성  


<br/>
<br/>

## ✨ 주요 기능

- 회원가입 / 로그인 (JWT 기반)  
- Access / Refresh Token 재발급  
- 개인 일정 CRUD  
- 일정 태그 및 아이콘 지정  
- 날짜 필터 기반 조회  
- Swagger API 문서화  
- CI/CD 자동 배포  
- Git Flow + Issue 기반 협업  

<br/>
<br/>
<br/>


## 🔐 인증 시스템

### ✔️ JWT 구조

- Access Token (30분 유효)  
- Refresh Token (Redis 저장, 2주 유효)  

### ✔️ 토큰 흐름

```
[Client] -- 로그인 --> [Server] → Access + Refresh 발급
                                 ↓
                             [Redis 저장]
```

- Access Token 만료 시 Redis에서 Refresh 검증 후 재발급  
- 로그아웃 시 Redis에서 Refresh 삭제  


<br/>
<br/>
<br/>


## 🚀 CI/CD 및 배포

### ✅ 자동화 흐름 (GitHub Actions)

```yaml
on:
  push:
    branches: [main]
jobs:
  build-and-deploy:
    steps:
      - Checkout
      - JDK 21 설정
      - Gradle Build + Test
      - Docker Build
      - Push to GCP Artifact Registry
      - Deploy to GCP Cloud Run
```

- `main` 브랜치 Push 시 자동 트리거  
- GCR → Cloud Run 배포 자동화 완료  


<br/>
<br/>
<br/>


## 🔄 Git 전략 및 협업 관리

### 🌿 Git Flow 브랜치 전략

| 브랜치        | 용도                                |
|---------------|-------------------------------------|
| `main`        | 실제 배포 버전 유지                  |
| `develop`     | 개발 통합 브랜치                     |
| `feature/*`   | 기능 개발 브랜치 (develop에서 분기)   |
| `release/*`   | 배포 준비 브랜치 (develop → main 전) |
| `hotfix/*`    | 긴급 수정 (main → develop 병합)      |

```bash
# 예시
git checkout develop
git checkout -b release/1.0.0
```

<br/>

### 💬 PR 리뷰 및 Issue 관리

- GitHub Issue 생성 → 해당 이슈와 연결된 `feature/*` 브랜치 개발  
- PR 작성 시 관련 이슈 태그 + 변경 요약 + 테스트 내역 기재  
- PR은 `develop` 브랜치 기준으로 코드 리뷰 후 병합

<br/>

### 😺 Gitmoji 커밋 컨벤션

| 이모지 | 의미          | 예시 메시지                         |
|--------|---------------|--------------------------------------|
| ✨     | 새로운 기능    | `✨ 일정 생성 기능 추가`             |
| 🐛     | 버그 수정      | `🐛 토큰 재발급 오류 수정`           |
| ♻️     | 리팩토링       | `♻️ Redis 연결 방식 개선`           |
| 🔧     | 설정 수정      | `🔧 Docker 환경 변수 설정`           |
| 📝     | 문서 작성      | `📝 README 배포 방법 추가`           |


<br/>
<br/>
<br/>


## 🗃️ ERD 및 DB 설계

- `users`: 사용자 (이메일 기반 로그인)  
- `schedules`: 일정 (users와 1:N)  
- `tags`: 일정 분류 태그  
- `icons`: 태그 아이콘  
- Refresh Token은 Redis에서 관리  

📌 ERD 이미지: `/docs/erd.png` 혹은 Notion/Whimsical 기반 문서


<br/>
<br/>
<br/>


## 🧪 실행 방법

### 1. `.env` 설정

```
JWT_SECRET=your_secret_key
DATABASE_URL=jdbc:postgresql://...
REDIS_HOST=localhost
```

### 2. 로컬 실행 (Docker)

```bash
docker compose up --build
```

### 3. Swagger 문서 접속

[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)


<br/>
<br/>
<br/>


## 🛠 트러블슈팅

| 문제 | 해결 |
|------|------|
| PostgreSQL 세션 초과 | Supabase는 세션 수 제한 → 유휴 세션 수동 정리 |
| Redis 연결 오류 | Docker 네트워크 충돌 → 포트 변경 및 컨테이너 재실행 |
| Access Token 만료 | Redis에서 Refresh로 재발급 로직 누락 → 핸들러 추가 |
| GitHub Actions 실패 | 환경변수 누락 → GCP 서비스 계정 JSON 및 SECRET 등록 |


<br/>
<br/>
<br/>


## 📈 향후 개선 사항

- [ ] 일정 반복 기능 (예: 매주 화요일 반복)  
- [ ] 사용자 타임존 설정 및 로컬 시간 변환 처리  
- [ ] Webhook 또는 이메일 기반 일정 알림  
- [ ] 단위 및 통합 테스트 코드 작성  
- [ ] Prometheus + Grafana 기반 모니터링 도입  


<br/>
<br/>
<br/>


## 🙋‍♀️ 개발자

**이서연 | 백엔드 PL**  
연세대학교 미래캠퍼스 소프트웨어학부  
📫 GitHub: [github.com/ETi0n](https://github.com/ETi0n)  
📝 Blog: [velog.io/@etion](https://velog.io/@etion/posts)  
🎯 목표: 

**왕우 | 백엔드 PA**  
연세대학교 미래캠퍼스 소프트웨어학부  
📫 GitHub: []()  
📝 Blog: []()  
🎯 목표: 


<br/>
<br/>
<br/>


## 📎 라이선스

> 본 프로젝트는 학습 및 포트폴리오 목적이며, 자유롭게 참고 및 수정 가능합니다.  
> © 2025 DoIt2 팀. All rights reserved.
