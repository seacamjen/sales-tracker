--
-- PostgreSQL database dump
--

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: categories; Type: TABLE; Schema: public; Owner: Guest; Tablespace: 
--

CREATE TABLE categories (
    id integer NOT NULL,
    name character varying
);


ALTER TABLE categories OWNER TO "Guest";

--
-- Name: categories_id_seq; Type: SEQUENCE; Schema: public; Owner: Guest
--

CREATE SEQUENCE categories_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE categories_id_seq OWNER TO "Guest";

--
-- Name: categories_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: Guest
--

ALTER SEQUENCE categories_id_seq OWNED BY categories.id;


--
-- Name: customers; Type: TABLE; Schema: public; Owner: Guest; Tablespace: 
--

CREATE TABLE customers (
    id integer NOT NULL,
    email character varying,
    name character varying,
    admin boolean
);


ALTER TABLE customers OWNER TO "Guest";

--
-- Name: customers_id_seq; Type: SEQUENCE; Schema: public; Owner: Guest
--

CREATE SEQUENCE customers_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE customers_id_seq OWNER TO "Guest";

--
-- Name: customers_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: Guest
--

ALTER SEQUENCE customers_id_seq OWNED BY customers.id;


--
-- Name: products; Type: TABLE; Schema: public; Owner: Guest; Tablespace: 
--

CREATE TABLE products (
    id integer NOT NULL,
    name character varying,
    price integer,
    description character varying,
    categoryid integer
);


ALTER TABLE products OWNER TO "Guest";

--
-- Name: products_id_seq; Type: SEQUENCE; Schema: public; Owner: Guest
--

CREATE SEQUENCE products_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE products_id_seq OWNER TO "Guest";

--
-- Name: products_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: Guest
--

ALTER SEQUENCE products_id_seq OWNED BY products.id;


--
-- Name: purchases; Type: TABLE; Schema: public; Owner: Guest; Tablespace: 
--

CREATE TABLE purchases (
    id integer NOT NULL,
    customerid integer,
    productid integer,
    purchasedate timestamp without time zone
);


ALTER TABLE purchases OWNER TO "Guest";

--
-- Name: purchases_id_seq; Type: SEQUENCE; Schema: public; Owner: Guest
--

CREATE SEQUENCE purchases_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE purchases_id_seq OWNER TO "Guest";

--
-- Name: purchases_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: Guest
--

ALTER SEQUENCE purchases_id_seq OWNED BY purchases.id;


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: Guest
--

ALTER TABLE ONLY categories ALTER COLUMN id SET DEFAULT nextval('categories_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: Guest
--

ALTER TABLE ONLY customers ALTER COLUMN id SET DEFAULT nextval('customers_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: Guest
--

ALTER TABLE ONLY products ALTER COLUMN id SET DEFAULT nextval('products_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: Guest
--

ALTER TABLE ONLY purchases ALTER COLUMN id SET DEFAULT nextval('purchases_id_seq'::regclass);


--
-- Data for Name: categories; Type: TABLE DATA; Schema: public; Owner: Guest
--

COPY categories (id, name) FROM stdin;
1	Apparel
2	Footwear
3	Equipment
\.


--
-- Name: categories_id_seq; Type: SEQUENCE SET; Schema: public; Owner: Guest
--

SELECT pg_catalog.setval('categories_id_seq', 3, true);


--
-- Data for Name: customers; Type: TABLE DATA; Schema: public; Owner: Guest
--

COPY customers (id, email, name, admin) FROM stdin;
2	chancemagno@gmail.com	Chance	f
3	Stormi@yahoo.mail	Storm	f
1	randy@gmail.com	Randy	t
\.


--
-- Name: customers_id_seq; Type: SEQUENCE SET; Schema: public; Owner: Guest
--

SELECT pg_catalog.setval('customers_id_seq', 4, true);


--
-- Data for Name: products; Type: TABLE DATA; Schema: public; Owner: Guest
--

COPY products (id, name, price, description, categoryid) FROM stdin;
2	Jersey	20	Timbers Jersey	1
5	Nike Mercurial Vapor XI	305	Built for the fastest players on the field.	2
6	Nike Tiempo Legend V SG-Pr	105	The Nike Tiempo Legend V SG-Pro is designed for superior touch in all conditions and unrivalled comfort for the modern warrior. Foruse on soft grass surfaces.	2
7	Nike Skills Ciento Soccer Ball	9	Develop your skills with Nike.	3
8	adidas Fracas Official Match Ball UEFA Euro 2016	100	FIFA-approved top-level match ball. Responds with accuracy for a predictable response for every strike.	3
9	adidas Krasava FIFA Confederations Cup Winter Official Match Soccer Ball	160	Hi-Vis version to stand out in snowy weather. Featuring a bespoke design, the Krasava FIFA Confederations Cup Official Match Ballsava employs the same tech as the 2014 World Cup Brazuca ball with a six panel construction.	3
3	adidas ACE 17+	175	The first ever laceless cleat has now received its first upgrade.	2
4	Scarf	45	Timbers Scarf	3
\.


--
-- Name: products_id_seq; Type: SEQUENCE SET; Schema: public; Owner: Guest
--

SELECT pg_catalog.setval('products_id_seq', 9, true);


--
-- Data for Name: purchases; Type: TABLE DATA; Schema: public; Owner: Guest
--

COPY purchases (id, customerid, productid, purchasedate) FROM stdin;
1	1	3	2017-04-04 14:29:56.572217
2	2	1	2017-04-04 14:37:56.622255
3	2	3	2017-04-04 14:38:40.556461
4	2	4	2017-04-04 14:39:05.119245
5	2	4	2017-04-04 14:39:10.994689
6	3	1	2017-04-04 14:42:43.106063
7	4	2	2017-04-04 14:49:44.750819
8	1	3	2017-03-01 00:00:00
\.


--
-- Name: purchases_id_seq; Type: SEQUENCE SET; Schema: public; Owner: Guest
--

SELECT pg_catalog.setval('purchases_id_seq', 8, true);


--
-- Name: categories_pkey; Type: CONSTRAINT; Schema: public; Owner: Guest; Tablespace: 
--

ALTER TABLE ONLY categories
    ADD CONSTRAINT categories_pkey PRIMARY KEY (id);


--
-- Name: customers_pkey; Type: CONSTRAINT; Schema: public; Owner: Guest; Tablespace: 
--

ALTER TABLE ONLY customers
    ADD CONSTRAINT customers_pkey PRIMARY KEY (id);


--
-- Name: products_pkey; Type: CONSTRAINT; Schema: public; Owner: Guest; Tablespace: 
--

ALTER TABLE ONLY products
    ADD CONSTRAINT products_pkey PRIMARY KEY (id);


--
-- Name: purchases_pkey; Type: CONSTRAINT; Schema: public; Owner: Guest; Tablespace: 
--

ALTER TABLE ONLY purchases
    ADD CONSTRAINT purchases_pkey PRIMARY KEY (id);


--
-- Name: public; Type: ACL; Schema: -; Owner: epicodus
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM epicodus;
GRANT ALL ON SCHEMA public TO epicodus;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- PostgreSQL database dump complete
--

