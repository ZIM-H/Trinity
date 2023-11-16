using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class TutorialController : MonoBehaviour
{
    // Start is called before the first frame update
    public int tutorialIndex = 0;
    void Start()
    {
        
    }

    public void GoRight(){
        int childCount = transform.childCount;
        if(tutorialIndex < childCount - 4){
        transform.GetChild(tutorialIndex+1).gameObject.SetActive(true);
        transform.GetChild(tutorialIndex).gameObject.SetActive(false);
        tutorialIndex ++;
        }
    }
    public void GoLeft(){
        Debug.Log(tutorialIndex);
        if(tutorialIndex > 0){
            transform.GetChild(tutorialIndex-1).gameObject.SetActive(true);
            transform.GetChild(tutorialIndex).gameObject.SetActive(false);
            tutorialIndex --;
        }
    }
    

    // Update is called once per frame
    void Update()
    {
        
    }
}
