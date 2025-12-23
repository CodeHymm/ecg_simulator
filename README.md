
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
