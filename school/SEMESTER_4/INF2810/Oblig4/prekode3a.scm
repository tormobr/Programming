;;;;
;;;; diverse hjelpekode for innlevering (3a) i INF2810, 2017.
;;;;

;;;
;;; grensesnitt for strømmer på samme måte som i seksjon 3.4 i SICP.
;;;

(define-syntax
  cons-stream
  (syntax-rules ()
    ((cons-stream head tail) (cons head (delay tail)))))

(define (stream-car stream) 
    (car stream))

(define (stream-cdr stream) 
  (force (cdr stream)))

;;;
;;; merk at `force' (prosedyre) og `delay' (special form) er innebygd i R5RS.
;;;

(define the-empty-stream '())

(define (stream-null? stream) 
  (null? stream))


;;;
;;; noen hjelpeprosedyrer til de ulike deloppgave, og diverse listeoperasjoner
;;; tilpasset strømmer; bruker prikk-notasjon for å la .n. være opsjonalt.
;;;

(define (show-stream stream . n)
  ;;
  ;; titt på de .n. første elementene i .stream.
  ;;
  (define (iter stream i)
    (cond ((= i 0) (display "...\n"))
          ((stream-null? stream)  (newline))
          (else (display (stream-car stream))
                (display " ")
                (iter (stream-cdr stream) (- i 1)))))
  (iter stream (if (null? n) 15 (car n))))


(define (stream-filter pred stream)
  (cond ((stream-null? stream) the-empty-stream)
        ((pred (stream-car stream))
         (cons-stream (stream-car stream)
                      (stream-filter pred
                                     (stream-cdr stream))))
        (else (stream-filter pred (stream-cdr stream)))))


(define (stream-ref stream n)
  ;;
  ;; hent ut (returner) element på posisjon .n. i .s.
  ;;
  (if (= n 0)
      (stream-car stream)
      (stream-ref (stream-cdr stream) (- n 1))))


(define (stream-interval low high)
  (if (> low high)
      the-empty-stream
      (cons-stream
       low
       (stream-interval (+ low 1) high))))

(define (show x) 
  (display x)
  (newline)
  x)

(define (add-streams s1 s2)
  ;;
  ;; elementvis addisjon av to strømmer; forutsetter generalisert `stream-map'
  ;; (som polyadisk prosedyre, dvs. med variabelt antall parametre)
  ;;
  (stream-map + s1 s2))


;;;
;;; naturlige tall som en uendelig strøm
;;;
(define (integers-starting-from n)
  (cons-stream n (integers-starting-from (+ n 1))))

(define nats (integers-starting-from 1))


;;;
;;; 
(define (read-corpus file)
  
  (define (skip port)
    ;;
    ;; advance .port. to first non-whitespace position (or EOF)
    ;;
    (let ((c (peek-char port)))
      (cond ((and (not (eof-object? c)) (char-whitespace? c))
             (read-char port)
             (skip port)))))
  
  (define (read-token port)
    ;;
    ;; read one whitespace-separated token from .port.
    ;;
    (define (recurse port characters)
      (let ((c (peek-char port)))
        (if (or (char-whitespace? c) (eof-object? c))
          characters
          (recurse port (cons (read-char port) characters)))))
    (skip port)
    (list->string (reverse (recurse port '()))))

  (define (read-sentence port)
    ;;
    ;; read one newline-separated sequence of tokens from .port.
    ;;
    (define (recurse port tokens)
      (let ((c (peek-char port)))
        (if (or (eof-object? c) (char=? c #\newline))
          (reverse (cons "</s>" tokens))
          (let ((token (read-token port)))
            (recurse port (cons token tokens))))))
    (skip port)
    (recurse port (list "<s>")))
  
  (define (recurse port sentences)
    ;;
    ;; our main driver function, iterate through lines of input from .port.
    ;;
    (let ((sentence (read-sentence port)))
      (if (null? (cddr sentence))
        (reverse sentences)
        (recurse port (cons sentence sentences)))))

  (define (start port)
    ;;
    ;; initiate iteration over lines of input from .port.
    ;;
    (recurse port '()))
  
  ;;
  ;; connect a port to .file. and have .start. invoked with the .port. as its
  ;; sole argument; .port. will be closed upon exit from call-with-input-file.
  ;; 
  (call-with-input-file file start))

