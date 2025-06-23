# BE

| 카테고리   | 기술/서비스                  | 설명                        |
| ------ | ----------------------- | ------------------------- |
| 언어     | Java 21                 | 최신 LTS 버전                 |
| 프레임워크  | Spring Boot 3.x         | REST API, 보안 등            |
| ORM    | Spring Data JPA         | 객체-관계 매핑                  |
| 인증     | Spring Security + JWT   | Access + Refresh Token 기반 |
| 문서화    | springdoc-openapi       | Swagger UI 자동 생성          |
| 마이그레이션 | Flyway                  | DB 버전 관리 및 이력 추적          |
| 로깅     | Logback                 | 파일/콘솔 기반 로깅               |
| 테스트    | JUnit5 + Testcontainers | PostgreSQL 연동 테스트 환경      |

<br/>

| 항목      | 서비스                       | 설명                           |
| ------- | ------------------------- | ---------------------------- |
| RDBMS   | PostgreSQL (via Supabase) | 클라우드 DB + 무료 티어              |
| 파일 스토리지 | Cloudflare R2             | S3 호환, Signed URL 방식 이미지 업로드 |

<br/>

| 항목          | 서비스                    | 설명                     |
| ----------- | ---------------------- | ---------------------- |
| 배포 플랫폼      | Render                 | Spring Boot 무료 배포      |
| CI/CD       | GitHub Actions         | 자동 테스트 & 빌드 & 배포       |
| API Gateway | NGINX (선택)             | 정적 라우팅, CORS, HTTPS 처리 |
| 모니터링        | Prometheus + Grafana   | 서버 메트릭 수집 및 시각화        |
| 환경 변수 관리    | `.env` + `java-dotenv` | 보안 분리 및 실행 환경 설정       |

<br/>

| 항목      | 도구/라이브러리             | 설명                  |
| ------- | -------------------- | ------------------- |
| Git 관리  | GitHub + Gitmoji     | 브랜치 전략 + 커밋 컨벤션 적용  |
| API 테스트 | Postman / Swagger UI | 클라이언트 연동용           |
| 데이터 설계  | dbdiagram.io         | ERD 시각화 및 공유        |
| 문서화     | Notion + Markdown    | 프로젝트 구조, 명세, 회의록 정리 |
