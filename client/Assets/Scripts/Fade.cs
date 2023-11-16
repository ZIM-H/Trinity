using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using DG.Tweening;
using UnityEngine.UI;
public class Fade : MonoBehaviour
{
    // Start is called before the first frame update
    void Start()
    {
        GetComponent<Image>().DOFade(0,1.0f);
    }

    // Update is called once per frame
    void Update()
    {
        
    }
    public void FadeOut(){
        GetComponent<Image>().DOFade(1, 1.0f);
    }

}
