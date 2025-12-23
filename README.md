


---

```md
# ECG Wave Simulator

Java 기반 **ECG Waveform 시뮬레이터**입니다.  
실제 의료기기 없이도 **250Hz ECG 데이터**를 생성하여  
Netty 기반 서버 및 CMS 시스템을 테스트할 수 있도록 설계되었습니다.

이 프로젝트는 **가상 ECG 디바이스(Device Simulator)** 역할을 수행하며,  
실제 장비와 유사한 **바이너리 패킷(16 bytes)** 을 주기적으로 전송합니다.

---

## ✨ 주요 특징

- ✅ **250Hz ECG Waveform 생성**
- ✅ **16바이트 바이너리 패킷 전송**
- ✅ **Netty TCP Server 연동**
- ✅ **부정맥 시뮬레이션 지원 (확장 가능)**
- ✅ UI 없이도 CMS/Backend 테스트 가능
- ✅ 실제 의료기기 테스트 흐름과 유사한 구조

---

## 🏗️ 전체 구조

```

[ECG Simulator (Java)]

* ECG Wave Generator
* Arrhythmia Controller
* Packet Encoder (16 bytes)
  ↓
  [Netty Server]
  ↓
  [CMS / Ingestor / Transformer / OLAP]

````

> 향후 UI(WebSocket + React)를 추가하여  
> 실시간 파형 시각화 및 시뮬레이터 제어 기능 확장 예정입니다.

---

## 📦 패킷 구조 (16 bytes)

| Offset | Size | Description |
|------|------|-------------|
| 0 ~ 1 | 2 | ECG Sample #1 (int16) |
| 2 ~ 3 | 2 | ECG Sample #2 |
| 4 ~ 5 | 2 | ECG Sample #3 |
| 6 ~ 7 | 2 | ECG Sample #4 |
| 8 ~ 9 | 2 | ECG Sample #5 |
| 10 ~ 11 | 2 | ECG Sample #6 |
| 12 | 1 | Lead Type (e.g. Lead II) |
| 13 | 1 | Packet Sequence |
| 14 ~ 15 | 2 | Reserved |

- 패킷당 ECG 샘플 수: **6 samples**
- 전송 주기: **약 24ms**
- 결과 샘플링 레이트: **250Hz**

---

## 🫀 ECG Waveform 생성

### 기본 (Normal Sinus Rhythm)
- P wave + QRS complex + T wave 합성
- HR 기본값: 60 bpm

### 부정맥 (확장 가능)
- PVC (Premature Ventricular Contraction)
- AF (Atrial Fibrillation)
- VT (Ventricular Tachycardia)
- Pause / Asystole

> 현재는 코드 기반으로 리듬 변경  
> 향후 UI 또는 외부 제어 인터페이스로 확장 가능

---

## ⚙️ 기술 스택

- **Java 21**
- **Netty (TCP Client)**
- **Gradle**
- SLF4J

---

## ▶️ 실행 방법

### 1. Netty Server 실행
이미 준비된 Netty 서버를 먼저 실행합니다.

### 2. Simulator 실행

```bash
./gradlew run
````

실행 시:

* Netty 서버로 16바이트 ECG 패킷을 주기적으로 전송
* CMS에서 실제 장비 데이터처럼 수신 가능

---

## 🎯 활용 목적

* CMS / Backend 개발 중 **의료기기 없이 테스트**
* ECG 파형 파싱 및 타이밍 검증
* 알람 / 트렌드 / 저장 로직 검증
* 데모 및 교육용 시뮬레이터

---

## 🚀 향후 계획

* [ ] WebSocket 기반 UI 연동
* [ ] 실시간 ECG 파형 시각화
* [ ] UI에서 HR / Arrhythmia 제어
* [ ] 멀티 채널 ECG 지원 (Lead I/II/III)
* [ ] bin 파일 dump 및 replay 기능

---

## 📌 Note

이 프로젝트는 **의료기기 시뮬레이션 및 테스트 목적**으로 제작되었으며,
실제 진단이나 의료 행위를 대체하지 않습니다.

---

## 👤 Author

* Backend / Data Engineer
* Medical Device Data Pipeline & Real-time System

```

---

