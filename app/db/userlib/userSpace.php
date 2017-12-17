<?php

class UserSpace {

  public $root = '/tmp';
  public $userId = 'test-user';
  public $projects = [];
  public $currentSize = 0;

  public function __construct($userId, $root) {
    $this->$userId = $userId;
    $this->$root = $root;
    if(!is_dir($this->$root))
      mkdir($this->$root, 0776, true);
  }



}