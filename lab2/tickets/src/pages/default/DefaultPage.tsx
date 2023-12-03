import * as React from 'react';
import * as classNames from 'classnames';
import { useState } from 'react';
import styles from './index.module.scss';
import { Ticket } from '../../view/components/Ticket';
import { useGetTicketsQuery } from '../../core/api/ticket';
import { AddTicketForm } from '../../view/components/AddTicketForm';
import { DeleteTicketForm } from '../../view/components/DeleteTicketForm';
import { UpdateTicketForm } from '../../view/components/UpdateTicketForm';
import { SortByIdForm } from '../../view/components/SortByIdForm';
import { ISortTicketsType } from '../../core/api/ticket/types/ITicketType.ts';
import { Pagination } from '../../view/components/Pagination';
import { GetVenueAmountForm } from '../../view/components/GetVenueAmountForm';
import { GetUniquePricesForm } from '../../view/components/GetUnuquePricesForm';
import { DeleteTicketByVenueForm } from '../../view/components/DeleteTicketByVenueForm';
import { DeleteEventForm } from '../../view/components/DeleteEventForm';
import { DeletePersonForm } from '../../view/components/DeletePersonForm';

const cx = classNames.bind(styles);

export const DefaultPage: React.FC = () => {
    const [id, setId] = useState<ISortTicketsType>({
        id: undefined,
        page: undefined,
        page_size: undefined,
    });
    const { data, isError, isLoading } = useGetTicketsQuery(id);

    if (isLoading) {
        return null;
    }
    return (
        <div className={cx(styles.wrapper)}>
            <div className={'flex flex-row flex-wrap gap-8'}>
                <AddTicketForm />
                <div className={'flex flex-col gap-8'}>
                    <DeleteTicketForm />
                    <DeleteEventForm />
                </div>
                <UpdateTicketForm />
                <div className={'flex flex-col gap-8'}>
                    <DeleteTicketByVenueForm />
                    <DeletePersonForm />
                </div>
            </div>
            <div className={'mt-10 flex flex-row flex-wrap gap-8'}>
                <SortByIdForm submitHandler={(id) => setId({ page: undefined, page_size: undefined, id })} />
                <Pagination submitHandler={(page, pageSize) => setId({ page, page_size: pageSize, id: undefined })} />
                <GetVenueAmountForm />
                <GetUniquePricesForm />
            </div>
            <div className={cx(styles.ticketsContainer)}>
                {!(isError || !data) ? (
                    data?.map((ticket) => {
                        return (
                            <Ticket
                                key={ticket.id}
                                id={ticket.id}
                                event={ticket.event}
                                name={ticket.name}
                                coordinates={ticket.coordinates}
                                price={ticket.price}
                                type={ticket.type}
                                venue={ticket.venue}
                                personId={ticket.personId}
                            />
                        );
                    })
                ) : (
                    <div className={'text-2xl'}>no data</div>
                )}
            </div>
        </div>
    );
};
