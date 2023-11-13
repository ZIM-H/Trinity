using System.Collections;
using System.Collections.Generic;
using Unity.VisualScripting;
using UnityEngine;
using UnityEngine.EventSystems;
using UnityEngine.SceneManagement;
public class TutorialButton : MonoBehaviour, IPointerClickHandler
{
    public int buttonType;
    // Start is called before the first frame update
    void Start()
    {
        
    }
    
    public void OnPointerClick(PointerEventData eventData){
        Debug.Log(buttonType);
        if(buttonType==2){
            SceneManager.LoadScene("mainScreen");
        }else if(buttonType == 1){
            transform.parent.GetComponent<TutorialController>().GoRight();
        }else if(buttonType == 3){
            transform.parent.GetComponent<TutorialController>().GoLeft();
        }
    }
    // Update is called once per frame
    void Update()
    {
        
    }
}
