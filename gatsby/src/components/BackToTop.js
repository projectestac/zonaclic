import React, { useState, useEffect } from 'react';
import Fab from '@material-ui/core/Fab';
import ArrowUp from '@material-ui/icons/ArrowUpward';

// Based on: https://github.com/donaldboulton/publiuslogic/blob/master/src/components/Scroll/index.js

export default function BackToTop({ intl, showBelow, style = {} }) {

  const { messages } = intl;
  const [show, setShow] = useState(showBelow ? false : true);

  const handleScroll = () => {
    if (window.pageYOffset > showBelow) {
      if (!show) setShow(true);
    } else {
      if (show) setShow(false);
    }
  }

  const handleClick = () => {
    window.scrollTo({
      top: 0,
      left: 0,
      behavior: 'smooth',
    });
  }

  useEffect(() => {
    if (showBelow) {
      window.addEventListener('scroll', handleScroll);
      return () => window.removeEventListener('scroll', handleScroll);
    }
  });


  return (
    <Fab
      title={messages['top']}
      size="small"
      onClick={handleClick}
      style={{
        visibility: show ? 'visible' : 'hidden',
        position: 'fixed',
        bottom: '1rem',
        right: '1rem',
        ...style,
      }}
    >
      <ArrowUp />
    </Fab>
  );

}
