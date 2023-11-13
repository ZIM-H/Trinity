using UnityEngine;
using UnityEngine.UI;
using UnityEngine.EventSystems;
public class JoyStick : MonoBehaviour, IDragHandler, IPointerUpHandler, IPointerDownHandler
{
    private Image jsContainer;
    private Image joystick;

    public Vector3 InputDirection;

    private AudioSource audioSource; // 오디오 소스 추가

    public AudioClip footstepSound; // 발소리 오디오 클립 할당

    void Start()
    {

        jsContainer = GetComponent<Image>();
        joystick = transform.GetChild(0).GetComponent<Image>(); 
        InputDirection = Vector3.zero;

        // 오디오 소스 컴포넌트 가져오기
        audioSource = GetComponent<AudioSource>();

        // 발소리 오디오 클립 할당
        audioSource.clip = footstepSound;


    }

    public void OnDrag(PointerEventData ped)
    {
        Vector2 position = Vector2.zero;

       
        RectTransformUtility.ScreenPointToLocalPointInRectangle
                (jsContainer.rectTransform,
                ped.position,
                ped.pressEventCamera,
                out position);

        position.x = (position.x / jsContainer.rectTransform.sizeDelta.x);
        position.y = (position.y / jsContainer.rectTransform.sizeDelta.y);

        float x = (jsContainer.rectTransform.pivot.x == 1f) ? position.x * 2 + 1 : position.x * 2 - 1;
        float y = (jsContainer.rectTransform.pivot.y == 1f) ? position.y * 2 + 1 : position.y * 2 - 1;

        InputDirection = new Vector3(position.x * 2 + 0, position.y * 2);
        InputDirection = (InputDirection.magnitude > 1) ? InputDirection.normalized : InputDirection;

        
        joystick.rectTransform.anchoredPosition = new Vector3(InputDirection.x * (jsContainer.rectTransform.sizeDelta.x / 3) , InputDirection.y * (jsContainer.rectTransform.sizeDelta.y) / 3);

        // x, y 좌표값이 변경되면 발소리 재생
        if (InputDirection.x != 0 || InputDirection.y != 0)
        {
            // 발소리 재생
            if (!audioSource.isPlaying)
            {
                audioSource.Play();
            }
        }

    }

    public void OnPointerDown(PointerEventData ped)
    {

        OnDrag(ped);
    }

    public void OnPointerUp(PointerEventData ped)
    {

        InputDirection = Vector3.zero;
        joystick.rectTransform.anchoredPosition = Vector3.zero;
    }
}