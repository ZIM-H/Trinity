using UnityEngine;
using Newtonsoft.Json.Linq;
using Newtonsoft.Json;

public class VariableManager : MonoBehaviour
{
    // 싱글톤 인스턴스
    public static VariableManager Instance { get; private set; }

    // 게임 내에서 필요한 변수들
    public string playerId;
    public string message;
    public string gameRoomId;
    public int fertilizerAmount;
    public int eventCode;
    public bool conflictAsteroid;
    public int roomNo;
    public int date;
    public int fertilizerAmountInRoom;
    public bool purifierStatus;
    public bool farmStatus;
    public int carbonCaptureTryCount;
    public bool fertilizerInput;
    public bool fertilizerProduce;
    public bool fertilizerUpgrade;
    public bool purifierFix;
    public bool co2CollectorFix;
    public bool centralGardenFix;
    public bool taurineProduce;
    public bool asteroidShotDown;
    public bool barrierUpgrade;


    private void Awake()
    {
        if (Instance == null)
        {
            Instance = this;
            DontDestroyOnLoad(gameObject);
            playerId = PlayerPrefs.GetString("PlayerId");
        }
        else
        {
            Destroy(gameObject);
        }
    }

    public void SetFirstDayData(string receivedMessage)
    {
        FirstDay firstDayData = JsonConvert.DeserializeObject<FirstDay>(receivedMessage);

        // 여기서 필요한 변수에 데이터 할당
        fertilizerAmount = firstDayData.fertilizerAmount;
        eventCode = firstDayData.eventCode;
        fertilizerUpgrade = firstDayData.fertilizerUpgrade;
        barrierUpgrade = firstDayData.barrierUpgrade;
        conflictAsteroid = firstDayData.conflictAsteroid;
        gameRoomId = firstDayData.gameRoomId;
        if (firstDayData.firstResponseDto != null) {
            roomNo = 1;
            purifierStatus = firstDayData.firstResponseDto.purifierStatus;
            fertilizerAmountInRoom = firstDayData.firstResponseDto.fertilizerAmount;
            message = firstDayData.firstResponseDto.message;
        } else if (firstDayData.secondResponseDto != null) {
            roomNo = 2;
            farmStatus = firstDayData.secondResponseDto.farmStatus;
            carbonCaptureTryCount = firstDayData.secondResponseDto.carbonCaptureTryCount;
            fertilizerAmountInRoom = firstDayData.secondResponseDto.fertilizerAmount;
            message = firstDayData.secondResponseDto.message;
        } else {
            roomNo = 3;
            fertilizerAmountInRoom = firstDayData.thirdResponseDto.fertilizerAmount;
            message = firstDayData.thirdResponseDto.message;
        }
        Debug.Log(Instance);
    }


    public class FirstDay
    {
        // JSON 데이터 구조에 맞게 필드 정의
        public FirstRoom firstResponseDto { get; set; }
        public SecondRoom secondResponseDto { get; set; }
        public ThirdRoom thirdResponseDto { get; set; }
        public int fertilizerAmount { get; set; }
        public int eventCode { get; set; }
        public bool fertilizerUpgrade { get; set; }
        public bool barrierUpgrade { get; set; }
        public bool conflictAsteroid { get; set; }
        public string gameRoomId { get; set; }
    }
    public class FirstRoom
    {
        public int fertilizerAmount { get; set; }
        public string message { get; set; }
        public bool purifierStatus { get; set; }
    }

    public class SecondRoom
    {
        public int fertilizerAmount { get; set; }
        public string message { get; set; }
        public bool farmStatus { get; set; }
        public int carbonCaptureTryCount { get; set; }
    }

    public class ThirdRoom
    {
        public int fertilizerAmount { get; set; }
        public string message { get; set; }
        // public bool asteroidStatus { get; set; }
    }

}
