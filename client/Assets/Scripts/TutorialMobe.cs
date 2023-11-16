using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.EventSystems;
using UnityEngine.SceneManagement;
public class TutorialMobe : MonoBehaviour, IPointerClickHandler
{
    // Start is called before the first frame update
    void Start()
    {
        
    }
    public void OnPointerClick(PointerEventData eventData){
        SceneManager.LoadScene("Tutorial");
    }
    // Update is called once per frame
    void Update()
    {
        
    }
}
