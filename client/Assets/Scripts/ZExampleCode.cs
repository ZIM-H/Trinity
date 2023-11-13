using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.EventSystems;

public class ZExampleCode : MonoBehaviour, IPointerClickHandler
{// Start is called before the first frame update
    public AudioClip[] music;
    AudioSource soundSource;
    void Start()
    {
        soundSource = GetComponent<AudioSource>();
    }

    public void OnPointerClick(PointerEventData eventData){
        Debug.Log("ya");
        soundSource.clip = music[1];
        soundSource.Play();
        // gameObject.SetActive(false);
    }


    // Update is called once per frame
    void Update()
    {
    }
}
