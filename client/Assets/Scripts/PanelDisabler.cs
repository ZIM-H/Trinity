using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.EventSystems;

public class PanelDisabler : MonoBehaviour, IPointerClickHandler
{
    public void OnPointerClick(PointerEventData eventData){
        Debug.Log("Panel Touched!");

        // ScorePanelDisabler 스크립트가 부착된 게임 오브젝트 찾기
        PanelDisabler panelDisabler = FindObjectOfType<PanelDisabler>();

        if (panelDisabler != null) {
            // 찾은 게임 오브젝트 비활성화
            panelDisabler.gameObject.SetActive(false);
        } else {
            Debug.Log("PanelDisabler 스크립트가 부착된 게임 오브젝트를 찾을 수 없음");
        }
    }
}
