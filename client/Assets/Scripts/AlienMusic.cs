using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class AlienMusic : MonoBehaviour
{   
    public AudioClip[] backgroundMusic;
    // Start is called before the first frame update
    void Start()
    {
        if(VariableManager.Instance.monsterDate <= VariableManager.Instance.date){
            GetComponent<AudioSource>().clip = backgroundMusic[1];
        }else{
            GetComponent<AudioSource>().clip = backgroundMusic[0];
        }
        GetComponent<AudioSource>().Play();
    }

    // Update is called once per frame
    void Update()
    {
        
    }
}
